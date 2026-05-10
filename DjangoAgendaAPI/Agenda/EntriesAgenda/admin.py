from django.contrib import admin  # Django admin interface
from .models import Agenda  # Import Agenda model from current app


# Register Agenda model in Django admin with custom configuration
@admin.register(Agenda)
class AgendaAdmin(admin.ModelAdmin):

    # Columns shown in the admin list view
    list_display = ['title', 'date_time', 'type']

    # Fields that can be searched in the admin search bar
    search_fields = ['title', 'type']

    # Sidebar filters for quick filtering by these fields
    list_filter = ['type', 'date_time']