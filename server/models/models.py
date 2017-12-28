from server.app import db


class IDMixin(db.Model):
    id = db.Column(db.Integer, primary_key=True, autoincrement=True, nullable=False)


class VenueUser(IDMixin):
    username = db.Column(db.String, nullable=False)
    password = db.Column(db.String, nullable=False)
    email = db.Column(db.String, nullable=False)
    display_name = db.Column(db.String, nullable=False)


class Instance(IDMixin):
    name = db.Column(db.String, nullable=False)
    server_secret = db.Column(db.String, nullable=False)
    venue_user_id = db.Column(db.Integer, nullable=False)

    venue_user = db.relationship('VenueUser')
