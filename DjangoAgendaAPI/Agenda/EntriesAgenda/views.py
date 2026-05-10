from rest_framework.viewsets import ModelViewSet # Provides default CRUD operations (Create, Read, Update, Delete)

from .serializers import AgendaSerializer # Serializer that converts Agenda model <-> JSON
from .models import Agenda # Agenda database model


class AgendaViewSet(ModelViewSet):
    queryset = Agenda.objects.all() # Get all Agenda records from database
    
    # Defines how data is converted between model and JSON
    serializer_class = AgendaSerializer
