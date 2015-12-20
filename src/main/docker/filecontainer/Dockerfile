FROM ubuntu
MAINTAINER Michael Obermueller

RUN adduser jboss

USER jboss
RUN mkdir -p /tmp/images/mymusic

# Add VOLUME as datacontainer
VOLUME  /tmp/images/mymusic

ENTRYPOINT /usr/bin/tail -f /dev/null