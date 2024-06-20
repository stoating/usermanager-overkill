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

  - pre-production
    - rename 'update_me.env' to '.env'
    - ``docker-compose up``
      - the first time you do this, it will take a while

## production

- as a note. while running upgrades the screen may turn scary purple. this is normal. keep hitting enter.

- in order for the certbot to work, you need an a record pointing to your server
- after setting the A record, you will need to wait for the dns to propagate
- for different services this can take anywhere from 5 minutes to 72 hours
- you can check the propagation with ``dig A <your domain>``

- ssh into your server
- make your server up to date
  - ``sudo apt upgrade``
  - ``sudo reboot``
- copy the server-setup.sh to your server
  - ``scp -v server-setup.sh root@<server_ip_address>:~``
  - ssh onto the server
  - ``ssh root@<server_ip_address>``
  - run that bad boy
  - ``bash server-setup.sh``
  - reboot that bad boy
  - ``reboot``

- from your server, enable cloning from github
  - on the server, run ``ssh-keygen -t rsa -b 4096``
  - then you will need to add the public key we generate in the script to your github repo
  - ``cat ~/.ssh/id_rsa.pub`` and copy the output
  - on github: (your repo -> settings -> deploy keys) and add the copied key

- if you have memory issues (your server, i cant help you directly), you may want to add some swap space?
  - ``fallocate -l 1G /swapfile``
  - ``chmod 600 /swapfile``
  - ``mkswap /swapfile``
  - ``swapon /swapfile``
  - we then want to backup the /etc/fstab file
    - ``sudo cp /etc/fstab /etc/fstab.bak``
  - we then want to make it permanent
  - ``echo '/swapfile none swap sw 0 0' | sudo tee -a /etc/fstab``
  - then we want to set the swappiness
    - ``sudo sysctl vm.swappiness=10``
    - to make this permanent
    - ``echo 'vm.swappiness=10' | sudo tee -a /etc/sysctl.conf``

- now you need to clone the repository on the server to home/app
  - ``git config --global --add safe.directory /home/app && cd "/home/app" && git init && git remote add origin git@github.com:<user_name>/<repo_name>.git && git fetch && git checkout -t origin/main``
``

- now you will need to create an .env file on the server at /home/app/.env
  - ``cd /home/app && cp update_me.env .env``
  - edit the .env file to your liking
  - ``nano .env``
  - in my case, i needed to change the cpu architecture from amd64 to x64

- now you will need to run the docker-compose file
  - ``docker compose up -d``
  - you may need to mess around with commands like ``docker compose down`` and ``docker compose up --force-recreate``
  - you can clear out your old containers and volumes with:
  - ``docker system prune -a --volumes``

- now lets set up the ci/cd

## notes

- OS:
  - windows:
    - start WSL2
    - from WSL2, clone the repository
    - do _not_ clone to windows and mount the files in WSL2
  - other platforms:
    - you'll probably be fine

## todo

- CI/CD with github actions
- make amazing documentation
- add tests

## future

- setup server with ansible
- add mail
- add login
- add roles
- update front-end to clojurescript
