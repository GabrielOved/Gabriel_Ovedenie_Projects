from rest_framework.test import APITestCase  # Django REST Framework test client for API testing

from .models import Agenda  # Import Agenda model to create test data

class AgendaTestCase(APITestCase):
    # Runs before each test method to set up test data
    def setUp(self):
        # Create multiple Agenda objects in bulk for testing
        Agenda.objects.bulk_create([
            Agenda(title='Release 1', date_time='2026-05-03T14:30:00Z',type='Release'),
            Agenda(title='Reminder 1', date_time='2026-04-02T10:00:00Z',type='Reminder'),
            Agenda(title='Event 1', date_time='2026-06-15T00:00:00Z',type='Event'),
            Agenda(title='Release 2', date_time='2026-06-08T19:50:00Z',type='Release'),
            Agenda(title='Reminder 2', date_time='2026-05-03T21:40:00Z',type='Reminder'),
            Agenda(title='Event 2', date_time='2026-01-01T00:00:01Z',type='Event')
        ])

    # Test GET list endpoint (/api/Agenda/)
    def test_agenda_list(self):
        response = self.client.get('/api/Agenda/') # Call list endpoint
        self.assertEqual(response.status_code, 200)  # Ensure request is successful
        self.assertEqual(len(response.json()),6) # Ensure all items are returned

    # Test GET detail endpoint (/api/Agenda/<id>/)
    def test_agenda_detail(self):
        response = self.client.get('/api/Agenda/1/')  # Get first agenda item

        self.assertEqual(response.status_code, 200)  # Check request success
        self.assertEqual(response.json()['title'], 'Release 1')  # Validate title
        self.assertEqual(response.json()['date_time'], '2026-05-03T14:30:00Z')  # Validate datetime
        self.assertEqual(response.json()['type'], 'Release')  # Validate type 

    # Test POST (create new agenda item)
    def test_agenda_create(self):
        response = self.client.post('/api/Agenda/', {
            'title': 'Release',
            'date_time': '2026-05-03T00:00:00Z',
            'type': 'Release'
        })
        self.assertEqual(response.status_code, 201) # Created successfully
        self.assertEqual(response.json()['title'], 'Release')
        self.assertEqual(response.json()['date_time'], '2026-05-03T00:00:00Z')
        self.assertEqual(response.json()['type'], 'Release')

    # Test PUT (update existing agenda item)
    def test_agenda_update(self):
        response = self.client.put('/api/Agenda/1/', {
            'title': 'Release',
            'date_time': '2026-05-03T00:00:00Z',
            'type': 'Release'
        })
        self.assertEqual(response.status_code, 200) # Update successful
        self.assertEqual(response.json()['title'], 'Release')
        self.assertEqual(response.json()['date_time'], '2026-05-03T00:00:00Z')
        self.assertEqual(response.json()['type'], 'Release')

    # Test DELETE endpoint
    def test_agenda_delete(self):
        response = self.client.delete('/api/Agenda/1/') # Delete first item
        self.assertEqual(response.status_code, 204) # No content after delete
        self.assertEqual(len(Agenda.objects.all()), 5) # Ensure database now has one less item
        self.assertRaises(Agenda.DoesNotExist, Agenda.objects.get, pk=1) # Ensure deleted object no longer exists
        response = self.client.get('/api/Agenda/2/') # Ensure other items still exist
        self.assertEqual(response.status_code, 200)

     # Runs after each test to clean up database
    def tearDown(self):
        Agenda.objects.all().delete() # Remove all test data