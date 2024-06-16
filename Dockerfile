# start with an ubuntu image
FROM mcr.microsoft.com/devcontainers/base:jammy


# stage: install clojure"
# - update apt on the ubuntu box"
RUN apt update
RUN apt upgrade -y


# - install dependencies for clojure
RUN apt install bash
RUN apt install curl
RUN apt install rlwrap
RUN apt install openjdk-21-jre -y


# - install clojure.
# - see: https://clojure.org/guides/install_clojure
RUN curl --location --remote-name \
   https://github.com/clojure/brew-install/releases/latest/download/linux-install.sh
RUN chmod +x linux-install.sh
RUN sudo ./linux-install.sh
RUN sudo rm linux-install.sh


# stage: install babashka
# - see: https://github.com/babashka/babashka#installation
RUN curl --location --remote-name --silent \
    https://raw.githubusercontent.com/babashka/babashka/master/install
RUN chmod +x install
RUN ./install
RUN sudo rm install


COPY . /usr/src/app


WORKDIR /usr/src/app/projects/usermanager
RUN clojure -T:build uber
WORKDIR /usr/src/app
