from sickbeats.app import db


class IDMixin(object):
    id = db.Column(db.Integer, primary_key=True, autoincrement=True, nullable=False)
