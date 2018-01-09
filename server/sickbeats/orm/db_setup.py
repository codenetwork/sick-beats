from .models import *


def setup_data(db_session):
    _setup_platform_data(db_session)


def _setup_platform_data(db_session):
    if Platform.query.first() is None:
        # Add all of the supported platforms.
        db_session.add(Platform(name='Spotify'))
        db_session.commit()
