from django.contrib import admin
from .models import *
# Register your models here.
admin.site.register(Item)
admin.site.register(User)
admin.site.register(Charge)
admin.site.register(Reservation)

