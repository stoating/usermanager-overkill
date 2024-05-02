# See here for image contents: https://github.com/microsoft/vscode-dev-containers/tree/v0.140.1/containers/alpine/.devcontainer/base.Dockerfile

# defines
ARG POLYLITH_VERSION=0.2.19


# Start with an ubuntu image
FROM mcr.microsoft.com/devcontainers/base:jammy


# stage: install clojure"
#- update apt on the ubuntu box"
RUN apt update
RUN apt upgrade -y


#- install dependencies for clojure
RUN apt install bash
RUN apt install curl
RUN apt install rlwrap
RUN apt install openjdk-21-jre -y


#- install clojure.
#  see: https://clojure.org/guides/install_clojure
RUN curl --location --remote-name \
    https://github.com/clojure/brew-install/releases/latest/download/linux-install.sh
RUN chmod +x linux-install.sh
RUN sudo ./linux-install.sh


# stage: install polylith
# see: https://polylith.gitbook.io/poly/install/install
# this could be added as a dependency in the project, but it's nice to have it available by default
ARG POLYLITH_VERSION
RUN curl --location --remote-name \
    "https://github.com/polyfy/polylith/releases/download/v${POLYLITH_VERSION}/poly-${POLYLITH_VERSION}.jar" \
    && mkdir --parents /usr/local/polylith \
    && mv poly-$POLYLITH_VERSION.jar /usr/local/polylith \
    && bash -c cat <<EOF > /usr/local/bin/poly \
    && chmod +x /usr/local/bin/poly

#!/bin/bash
ARGS=""

while [ "\$1" != "" ] ; do
  ARGS="\$ARGS \$1"
  shift
done

exec "/usr/bin/java" \$JVM_OPTS "-jar" "/usr/local/polylith/poly-${POLYLITH_VERSION}.jar" \$ARGS
EOF


# stage: install babashka
# see: https://github.com/babashka/babashka#installation
RUN curl --location --remote-name --silent \
    https://raw.githubusercontent.com/babashka/babashka/master/install
RUN chmod +x install
RUN ./install


# stage: install clojurescript
RUN apt install nodejs -y
RUN apt install npm -y