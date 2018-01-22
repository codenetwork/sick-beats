from flask import Flask
from flask_sqlalchemy import SQLAlchemy

app = Flask(__name__)
app.config['SQLALCHEMY_DATABASE_URI'] = 'postgresql+psycopg2://sickbeats:sickbeats@localhost/sickbeats'
db = SQLAlchemy(app)


def run_app():
    from .orm.db_setup import setup_data

    setup_data(db.session)
    app.run(host='0.0.0.0', port=3000)