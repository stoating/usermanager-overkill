# todo

- CI/CD with github actions
- add tests

## prerequisites

- docker

## setup

- development
  - update env vars in .devcontainer/devcontainer.json
  - vscode
    - Command Palette -> Dev Containers: Rebuild and Reopen in Container
    - Command Palette -> Calva: Start a Project REPL and Connect (aka Jack-In)
    - development/src/user.clj -> evaluate 'start-app'

- production
  - update env vars in 'update_me.env"
  - rename 'update_me.env' to '.env'
  - ``docker-compose up``

## recommendations

- IDE:
  - vscode
    - Remote Containers Extension (for .devcontainer customizations)
  - other IDEs:
    - devcontainer cli ([docs](https://code.visualstudio.com/docs/devcontainers/devcontainer-cli))
    - help me fill this out for _your_ preferred IDE!

- OS:
  - Windows:
    - WSL2
    - for the love of all that is good and holy, just use WSL2
  - Other OSes:
    - you're probably fine

## notes

- OS:
  - windows:
    - start WSL2
    - from WSL2, clone the repository
    - do _not_ clone to windows and mount the files in WSL2
  - other platforms:
    - you'll probably be fine

- development
  - update env vars in .devcontainer/devcontainer.json
  - vscode
    - Command Palette -> Remote-Containers: Rebuild and Reopen in Container
  - other IDEs
    - ``devcontainer up --workspace-folder .``
    - follow something like [dev-containers-emacs](https://happihacking.com/blog/posts/2023/dev-containers-emacs/)

- standalone:
  - build with:
  - ``cd projects/usermanager && clojure -T:build uber``
  - run with:
  - ``java -jar projects/usermanager/target/usermanager-standalone.jar``

## hints

- installing devcontainer cli:
  - ``npm install -g @devcontainers/cli``

- in case you need node:
  - ``sudo apt update``
  - ``curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.39.1/install.sh | bash``
  - close, reopen terminal
  - ``nvm install node``
