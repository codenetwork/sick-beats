from passlib.hash import pbkdf2_sha512
import uuid

from sickbeats.app import db


class IDMixin(object):
    id = db.Column(db.Integer, primary_key=True, autoincrement=True, nullable=False)


class HashedPasswordMixin(object):
    password = db.Column(db.String, nullable=False)
    salt = db.Column(db.String, nullable=False)

    @classmethod
    def generate_salt(cls):
        return uuid.uuid4().hex

    @classmethod
    def hash_password(cls, input_password, salt):
        return pbkdf2_sha512.hash(input_password + salt)
