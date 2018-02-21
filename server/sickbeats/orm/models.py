from sickbeats.app import db
from .db_mixins import IDMixin, HashedPasswordMixin


class VenueUser(db.Model, IDMixin, HashedPasswordMixin):
    __tablename__ = 'venueuser'

    username = db.Column(db.String, nullable=False, unique=True)
    email = db.Column(db.String, nullable=False)
    display_name = db.Column(db.String, nullable=False)


class Instance(db.Model, IDMixin):
    __tablename__ = 'instance'

    name = db.Column(db.String, nullable=False)
    server_secret = db.Column(db.String, nullable=False, unique=True)
    venue_user_id = db.Column(db.Integer, db.ForeignKey('venueuser.id'), nullable=False)
    platform_id = db.Column(db.Integer, db.ForeignKey('platform.id'), nullable=False)

    venue_user = db.relationship('VenueUser')
    platform = db.relation('Platform')


class Artist(db.Model, IDMixin):
    __tablename__ = 'artist'

    name = db.Column(db.String, nullable=False)


class Album(db.Model, IDMixin):
    __tablename__ = 'album'

    name = db.Column(db.String, nullable=False)


class Platform(db.Model, IDMixin):
    __tablename__ = 'platform'

    name = db.Column(db.String, nullable=False, unique=True)


class Song(db.Model):
    __tablename__ = 'song'

    id = db.Column(db.String, primary_key=True, nullable=False)
    name = db.Column(db.String, nullable=True)
    album_id = db.Column(db.Integer, db.ForeignKey('album.id'), nullable=True)

    album = db.relationship('Album')


class SongArtistMapping(db.Model, IDMixin):
    __tablename__ = 'songartistmapping'

    song_id = db.Column(db.String, db.ForeignKey('song.id'), nullable=False)
    artist_id = db.Column(db.Integer, db.ForeignKey('artist.id'), nullable=False)

    song = db.relationship('Song')
    artist = db.relationship('Artist')


class AlbumArtistMapping(db.Model, IDMixin):
    __tablename__ = 'albumartistmapping'

    album_id = db.Column(db.Integer, db.ForeignKey('album.id'), nullable=False)
    artist_id = db.Column(db.Integer, db.ForeignKey('artist.id'), nullable=False)

    album = db.relationship('Album')
    artist = db.relationship('Artist')


class Queue(db.Model, IDMixin):
    __tablename__ = 'queue'

    song_id = db.Column(db.String, db.ForeignKey('song.id'), nullable=False)
