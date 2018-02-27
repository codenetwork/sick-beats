import socket
import sys
import os
import json
from threading import Thread

from sickbeats.app import db
from sickbeats import orm
from .common_messages import *

HOST = os.getenv('SERVER_HOST', '')
PORT = os.getenv('SERVER_POST', 57438)

sock = None


def client_listener(conn):
    while True:
        data = conn.recv(1024)
        if not data:
            close_message = 'Malformed packet'
            break
        message = json.loads(data)
        if 'type' not in message:
            close_message = 'Malformed packet'
            break
        print(message)
        message_type = message['type']
        if message_type == 'connect':
            if 'serverSecret' not in message:
                close_message = 'No server secret provided'
                break
            instance = db.query(orm.Instance).filter(orm.Instance.id == message['serverSecret']).first()
            if not instance:
                close_message = 'Incorrect server secret'
                break
            send_message(conn, {'type': 'platform', 'platform': instance.platform.name})
        if message_type == 'next':
            # The client has requested a song. Give from the queue, or make one up.
            pass
    close_packet = CLOSE_CONNECTION
    if close_message:
        close_packet.update({'message': close_message})
    send_message(conn, close_packet)
    conn.close()


def _socket_server():
    global sock
    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    try:
        sock.bind((HOST, PORT))
    except socket.error as e:
        print('Failed to bind to port: ' + e.strerror)
        sys.exit(-1)

    sock.listen(10)  # 10 for now. Give it a proper number later, maybe setup some sharding.

    print('Started socket listening on port {}'.format(PORT))

    while True:
        conn, addr = sock.accept()
        print('Connected with ' + addr[0] + ':' + str(addr[1]))
        Thread(target=client_listener, args=(conn,)).start()


def setup_socket_server():
    Thread(target=_socket_server).start()


def close_socker_server():
    # TODO
    pass
