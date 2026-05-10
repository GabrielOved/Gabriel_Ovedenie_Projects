from django.apps import AppConfig # Base class for Django app configuration


class EntriesAgendaConfig(AppConfig):
    # Default primary key field type for models in this app
    default_auto_field = 'django.db.models.BigAutoField'
    # Name of the Django application (must match app folder name)
    name = 'EntriesAgenda'

