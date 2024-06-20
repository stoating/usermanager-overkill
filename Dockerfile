# start with an ubuntu image
FROM mcr.microsoft.com/devcontainers/base:jammy


ARG APP_CPU_ARCH
RUN echo "underlying cpu arch: $APP_CPU_ARCH"


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


# stage: "install" clojurescript
RUN apt install nodejs -y
RUN apt install npm -y


# stage: install tailwind
RUN sudo curl --location --remote-name --silent \
    https://github.com/tailwindlabs/tailwindcss/releases/latest/download/tailwindcss-linux-${APP_CPU_ARCH}
RUN sudo chmod +x tailwindcss-linux-${APP_CPU_ARCH}
RUN sudo mv tailwindcss-linux-${APP_CPU_ARCH} /usr/local/bin/tailwindcss


# stage: copy project to container
COPY . /usr/src/app


# stage: generate tailwind css
WORKDIR /usr/src/app/bases/web/resources/tools/tailwind
RUN npx tailwindcss -i tailwind.css -o ../../public/css/tailwind_output.css


EXPOSE 8080


# stage: build clojure project
WORKDIR /usr/src/app/projects/usermanager
RUN clojure -T:build uber
WORKDIR /usr/src/app
