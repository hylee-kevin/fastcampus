from django.contrib import admin
from django.urls import include, path
from django.conf import settings
from django.conf.urls.static import static

from .views import hello

urlpatterns = [
    path('admin/', admin.site.urls),
    path('', hello, name='index'),
    # ... other URL patterns for your app ...
]

# Serve static files during development
if settings.DEBUG:
    urlpatterns += static(settings.STATIC_URL, document_root=settings.STATIC_ROOT)

