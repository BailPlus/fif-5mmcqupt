import sys
import requests
import json
import random
from PyQt5.QtWidgets import (
    QApplication, QWidget, QFormLayout, QLineEdit, QPushButton,
    QMessageBox, QVBoxLayout, QSpinBox, QTextEdit
)
from PyQt5.QtCore import QThread, pyqtSignal
import ctypes
from PyQt5.QtGui import QIcon
ctypes.windll.shell32.SetCurrentProcessExplicitAppUserModelID("5mm")


# ----------------- 基础功能函数 -----------------
def get_user_id(token: str) -> str:
    url = "https://moral.fifedu.com/kyxl-app/account/getUserInfo"
    headers = {
        "Authorization": token,
        "source": "10003"
    }
    response = requests.post(url, headers=headers)
    response.raise_for_status()
    data = response.json()
    return data["data"]["userId"]


def parse_unit_info(url: str) -> dict:
    query_string = url.split("?")[1]
    params = query_string.split("&")
    unitid = params[0].split("=")[1]
    taskId = params[1].split("=")[1]
    return {'unitid': unitid, 'taskid': taskId}


def get_levels(unitid: str, token: str) -> list:
    url = f"https://moral.fifedu.com/kyxl-app/stu/column/stuUnitInfo?unitId={unitid}"
    headers = {
        "source": "10003",
        "Authorization": token
    }
    response = requests.get(url, headers=headers)
    response.raise_for_status()
    data = response.json()
    level_list = data["data"]["levelList"]
    return [level["levelId"] for level in level_list]


def get_question_ids(level_ids: list, token: str) -> list:
    questions = []
    for level_id in level_ids:
        url = "https://moral.fifedu.com/kyxl-app/column/getLevelInfo"
        headers = {
            "Authorization": token,
            "source": "10003"
        }
        params = {"levelId": level_id}
        response = requests.post(url, headers=headers, data=params)
        response.raise_for_status()
        data = response.json()
        moshi = data["data"]["content"]["moshi"]
        question_data = moshi[1]["question"]
        question_id = question_data["questionid"]
        item_list = question_data["qcontent"]["item"]
        num_questions = len(item_list[0]["questions"])
        questions.append({
            "question_id": question_id,
            "number": num_questions  # 原始题目数，仅供参考
        })
    return questions


def generate_answer(question_id: str, num: int, min_score: int, max_score: int, time_sec: int) -> list:
    return [{
        "questionId": f"{question_id}#0#{num}",
        "semantic": random.randint(min_score, max_score),
        "accuracy": random.randint(min_score, max_score),
        "fluency": random.randint(min_score, max_score),
        "complete": random.randint(min_score, max_score),
        "score": random.randint(min_score, max_score),
        "ansDetail": "系统出错！请联系管理员！",
        "recordPath": "114514",
        "learn_time": random.randint(time_sec, time_sec + 3)
    }]


def submit_grades(questions: list, level_ids: list, user_id: str, task_id: str,
                  min_score: int, max_score: int, time_sec: int, submission_count: int):
    # 如果用户输入次数小于20，则强制提交20次
    final_count = submission_count if submission_count >= 20 else 20
    for idx, question in enumerate(questions):
        level_id = level_ids[idx]
        for i in range(final_count):
            result_json = generate_answer(question["question_id"], i, min_score, max_score, time_sec)
            url = "https://moral.fifedu.com/kyxl-app-challenge/evaluation/submitChallengeResults"
            headers = {
                "clientType": "6",
                "userId": user_id
            }
            data = {
                "levelId": level_id,
                "studentId": user_id,
                "taskId": task_id,
                "resultJson": json.dumps(result_json)
            }
            response = requests.post(url, headers=headers, data=data)
            response.raise_for_status()


# ----------------- 后台线程 -----------------
class Worker(QThread):
    finished = pyqtSignal(str)
    error = pyqtSignal(str)

    def __init__(self, token, url, min_score, max_score, time_sec, submission_count, parent=None):
        super().__init__(parent)
        self.token = token
        self.url = url
        self.min_score = min_score
        self.max_score = max_score
        self.time_sec = time_sec
        self.submission_count = submission_count

    def run(self):
        try:
            unit_info = parse_unit_info(self.url)
            unit_id = unit_info["unitid"]
            task_id = unit_info["taskid"]
            level_ids = get_levels(unit_id, self.token)
            questions = get_question_ids(level_ids, self.token)
            user_id = get_user_id(self.token)
            submit_grades(questions, level_ids, user_id, task_id,
                          self.min_score, self.max_score, self.time_sec, self.submission_count)
            self.finished.emit("已完成任务！")
        except Exception as e:
            self.error.emit(str(e))


# ----------------- 主窗口界面 -----------------
class MainWindow(QWidget):
    def __init__(self):
        super().__init__()
        self.setWindowTitle("成绩提交工具")
        self.resize(800, 600)
        self.setup_ui()
        self.setWindowIcon(QIcon('favicon.ico'))

    def setup_ui(self):
        layout = QVBoxLayout()
        form_layout = QFormLayout()

        self.token_input = QLineEdit()
        self.url_input = QLineEdit()
        self.min_input = QSpinBox()
        self.min_input.setRange(0, 100)
        self.max_input = QSpinBox()
        self.max_input.setRange(0, 100)
        self.time_input = QSpinBox()
        self.time_input.setRange(0, 10000)
        # 新增提交次数输入控件
        self.count_input = QSpinBox()
        self.count_input.setRange(1, 1000)
        self.count_input.setValue(20)  # 默认值为20

        form_layout.addRow("Token：", self.token_input)
        form_layout.addRow("URL：", self.url_input)
        form_layout.addRow("最低分：", self.min_input)
        form_layout.addRow("最高分：", self.max_input)
        form_layout.addRow("用时(sec)：", self.time_input)
        form_layout.addRow("提交次数：", self.count_input)

        layout.addLayout(form_layout)

        self.submit_button = QPushButton("提交")
        self.submit_button.clicked.connect(self.start_submission)
        layout.addWidget(self.submit_button)

        self.log_output = QTextEdit()
        self.log_output.setReadOnly(True)
        layout.addWidget(self.log_output)

        self.setLayout(layout)

    def start_submission(self):
        token = self.token_input.text().strip()
        url = self.url_input.text().strip()
        min_score = self.min_input.value()
        max_score = self.max_input.value()
        time_sec = self.time_input.value()
        submission_count = self.count_input.value()

        if not token or not url:
            QMessageBox.warning(self, "输入错误", "Token和URL不能为空！")
            return

        self.log_output.append("开始提交任务...")
        self.submit_button.setEnabled(False)
        self.worker = Worker(token, url, min_score, max_score, time_sec, submission_count)
        self.worker.finished.connect(self.on_finished)
        self.worker.error.connect(self.on_error)
        self.worker.start()

    def on_finished(self, message):
        self.log_output.append(message)
        QMessageBox.information(self, "完成", message)
        self.submit_button.setEnabled(True)

    def on_error(self, error_msg):
        self.log_output.append("错误：" + error_msg)
        QMessageBox.critical(self, "错误", error_msg)
        self.submit_button.setEnabled(True)


# ----------------- 主程序入口 -----------------
if __name__ == '__main__':
    app = QApplication(sys.argv)

    # 启动时的免责声明提示窗口
    disclaimer = QMessageBox()
    disclaimer.setIcon(QMessageBox.Warning)
    disclaimer.setWindowTitle("免责声明")
    disclaimer.setWindowIcon(QIcon('favicon.ico'))
    disclaimer.setText(
        "使用本软件前请注意：\n\n"
        "本软件由虫游5mm应援团制作，仅供学习参考！用于非法违规用途与制作者无关！\n"
        "使用者需对使用过程中产生的任何问题自行负责。\n"
        "请确认您已阅读并接受此免责声明。\n"
        "Github：https://github.com/Thanwinde/fif-5mmcqupt"
    )
    disclaimer.setStandardButtons(QMessageBox.Ok | QMessageBox.Cancel)
    if disclaimer.exec_() != QMessageBox.Ok:
        sys.exit(0)

    window = MainWindow()
    window.show()
    sys.exit(app.exec_())
