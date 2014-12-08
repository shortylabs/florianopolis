florianopolis
=============

Florianópolis public transporation search

Find public transportation routes by street name
For each route found, show a list of stops on that route, and the route's weekday, Saturday, and Sunday timetable

Why is the "Back" button missing from the detail screen even though the spec calls for one?
My implementation uses the Android "Up" navigation feature and also the default back navigation.
Luis had given me some freedom to "make a nice UI" and I thought that a Back button would detract from the user experience.

A possible usability issue is that the Weekday, Saturday, and Sunday tabs need to communicate that these are departure times from the route's starting point.  Users may think that the times correspond to the stops.  I tried using "Weekday departures", "Saturday departures", and "Sunday departures", but it took up too much screen space. 

An enhancement would be to provide a Setting to show times in 12 hour or 24 hour format, e.g., 15:09 would be shown as 3:00 PM in 12-hour mode.

Normally, I would use third party libraries, like Retrofit, but I wanted to show that I can use Java and Android libraries, too.



