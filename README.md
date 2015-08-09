# Showcase

Showcase is a skeleton project for a JavaEE web application that puts together
a bunch of frameworks and libraries commonly considered among the de facto
standards of the industry.

# Authors
Just me (merka) for the moment and for the forseeable future.

# Goals
The main goals of *Showcase* are:

1. creating an archetype to use as a personal starting point whenever the creation of a new web application is required;
2. having a reference project to look up for snippets of code and examples
whenever dealing with problems similar to the ones solved here;
3. having a "showcase" (from which the name...) to prove the knowledge of the most widespread and important technologies in the Java ecosystem, possibly useful in CVs and during job interviews;
4. having a tested (and working) source code base to copy and paste on answers on Stackoverflow, if that is appropriate;
100. learning something new in the process.

# What has been put together
*Showcase* is built around, and with, the following systems:

* Maven
* Git
* JUnit
* Spring
  * Spring Web
  * Spring MVC
  * Spring Security
  * Spring Test
* Thymeleaf (coming soon)
* JQuery
* Bootstrap
* Hibernate
* Jetty as an optional Web Server for easing development
* HyperSQL as the development DBMS

# Focus
While developing *Showcase* I kept in mind some basic needs that I always have in any projects:

* a test suite that works out of the box, with a Spring test context and a test database. I do not know about you, but I always have trouble making `@RunWith` and `@ContextConfiguration` work at the first take. Maybe I'm stupid, but I do not want to waste any more time on this;
* security: users authentication and authorization functions should already be working. That includes a custom login page (and form) and the integration with the database (not simply an in-memory user provider). The possible sharp corners due to the complexity of a Spring security configuration should already be solved;
* a common graphic theme has to be in place and it should be a pleasent one. Nowadays you don't have to look any further than Bootstrap. If you do not use it, you look naive. If you do, you look professional, even if you are a dork at graphics (just like I am);
* a basic setup of the ORM of choice (Hibernate) must be in place in order to remind you all the basics and to act as a template for any new entity.

# Something personal
The main force behind the development of *Showcase* has been the bitter realization that I had several gaps in my technical background that I could not afford anymore. These are the kind of gaps that usually strike you while reading questions and answers on Stackoverflow. When I browse through topics and tags to find some questions to answer and increase my reputation I am always confronted with the fact that I actually know very little about anything at all.

# License
*Showcase* is released under GPLv3 because I am deeply convinced that knowledge must be free for everyone and software is a form of knowledge. Stallman rules.
