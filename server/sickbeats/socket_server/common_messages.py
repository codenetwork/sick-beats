import json

CLOSE_CONNECTION = {'type': 'close'}


def send_message(conn, message_dict: dict) -> None:
    conn.send(json.dumps(message_dict).encode('utf-8'))
