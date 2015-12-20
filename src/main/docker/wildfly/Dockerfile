FROM jboss/wildfly

#ARG dbpassword

# Set some environment variables
ENV POSTGRES_VERSION 9.3-1102

# Add the jboss-cli commands for postgres
ADD commands.cli /tmp/commands.cli

# Download postgres, configure jboss-cli for parameter resolving, run jboss-cli, and clean-up
RUN cd $HOME \
    && curl -O https://jdbc.postgresql.org/download/postgresql-$POSTGRES_VERSION.jdbc4.jar \
    && mv $HOME/postgresql-$POSTGRES_VERSION.jdbc4.jar /tmp/ \
    && echo postgres.version=${POSTGRES_VERSION} > /tmp/env.properties \
    && $JBOSS_HOME/bin/jboss-cli.sh --properties=/tmp/env.properties --file=/tmp/commands.cli \
    && rm -rf $JBOSS_HOME/standalone/configuration/standalone_xml_history \
    && rm /tmp/postgresql-$POSTGRES_VERSION.jdbc4.jar /tmp/env.properties

# Add the application to deploy and start
ADD mymusic.war /opt/jboss/wildfly/standalone/deployments/

# Expose the ports we're interested in
EXPOSE 8080 9990

# Set the default entrypoint to run on boot
# This will boot WildFly in the standalone mode and bind to all interface
ENTRYPOINT ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0"]