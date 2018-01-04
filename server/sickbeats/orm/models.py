from sickbeats.app import db
from .db_mixins import IDMixin


class VenueUser(db.Model, IDMixin):
    __tablename__ = 'venueuser'

    username = db.Column(db.String, nullable=False)
    password = db.Column(db.String, nullable=False)
    email = db.Column(db.String, nullable=False)
    display_name = db.Column(db.String, nullable=False)


class Instance(db.Model, IDMixin):
    __tablename__ = 'instance'

    name = db.Column(db.String, nullable=False)
    server_secret = db.Column(db.String, nullable=False)
    venue_user_id = db.Column(db.Integer, nullable=False)

    venue_user = db.relationship('VenueUser')
