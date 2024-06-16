# Over-engineering the User Manager

## prerequisites

- docker

## recommendations

- This will work best with:
  - vscode (otherwise the .devcontainer will need to be modified and the development workflow will need to be adjusted)
    - Remote Development extension pack

- OS:
  - Windows:
    - WSL2
    - for the love of all that is good and holy, just use WSL2
  - Other OS's:
    - I'm sure you'll be fine

## setup

- clone repository
  - windows:
    - start WSL2
    - from WSL2, clone the repository
    - do _not_ clone to windows and mount the files in WSL2
  - Other OS's:
    - I'm sure you'll be fine

- run
  - development
    - vscode
      - Command Palette -> Dev Containers: Rebuild and Reopen in Container
        - the first time you do this, it will take a while
      - Command Palette -> Calva: Start a Project REPL and Connect (aka Jack-In)
      - development/src/user.clj -> evaluate 'start-app'
    - other IDEs
      - ``devcontainer up --workspace-folder .``
      - follow something like [dev-containers-emacs](https://happihacking.com/blog/posts/2023/dev-containers-emacs/)
      - help me fill this out for _your_ preferred IDE!

  - production
    - rename 'update_me.env' to '.env'
    - ``docker-compose up``
      - the first time you do this, it will take a while

## notes

- OS:
  - windows:
    - start WSL2
    - from WSL2, clone the repository
    - do _not_ clone to windows and mount the files in WSL2
  - other platforms:
    - you'll probably be fine

## todo

- better handling of env (env.edn)
- CI/CD with github actions
- make amazing documentation
- add tests

## future

- add mail
- add login
- add roles
- update front-end to clojurescript
