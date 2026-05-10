from rest_framework import serializers  # Django REST Framework serializers module

from .models import Agenda  # Import Agenda model


class AgendaSerializer(serializers.ModelSerializer):
    # Custom read-only field that is not stored in the database
    date = serializers.SerializerMethodField()

    class Meta:
        # Model this serializer is based on
        model = Agenda

        # Fields exposed in API response
        fields = ['id', 'title', 'date_time', 'type', 'date']


    # Custom method to define value of "date" field
    def get_date(self, obj):
        # Extract only the date part from the DateTimeField (YYYY-MM-DD)
        return obj.date_time.date()