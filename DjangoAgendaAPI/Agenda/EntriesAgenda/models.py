from django.db import models  # Django ORM models module
from django.utils import timezone  # Provides timezone-aware datetime utilities


class Agenda(models.Model):
    """
    Agenda model representing a scheduled item/event
    """

    # Title of the agenda item
    title = models.CharField(max_length=1000)

    # Date and time of the agenda item (defaults to current time)
    date_time = models.DateTimeField(default=timezone.now)

    # Predefined choices for agenda type
    TYPE_CHOICES = [
        ('Release', 'Release'),
        ('Reminder', 'Reminder'),
        ('Event', 'Event'),
    ]

    # Type of agenda item (restricted to TYPE_CHOICES)
    type = models.CharField(max_length=100, choices=TYPE_CHOICES)

    # String representation used in admin panel and debugging
    def __str__(self):
        return f'{self.title} ({self.date_time}) - {self.type}'