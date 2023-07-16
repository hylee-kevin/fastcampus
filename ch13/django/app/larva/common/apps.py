from django.apps import AppConfig
from django.shortcuts import render

class CommonConfig(AppConfig):
    default_auto_field = 'django.db.models.BigAutoField'
    name = 'common'

def index(request):
    return render(request, 'index.html')
