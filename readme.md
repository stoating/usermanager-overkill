# User Manager Overkill

----

"I'm here to _do_, not to _read_":

- have [docker](https://docs.docker.com/get-docker/) installed
- clone repository
- rename '.env.deleteme' to '.env'
- update .env to match your underlying platform (linux-arm64, linux-armv7, linux-x64 [for tailwind](https://github.com/tailwindlabs/tailwindcss/releases/tag/v3.4.4) you can find this with ``uname -m``)
- ``docker compose up``

----

This repo is a simple web application built with the following Clojure libraries:

- Aero (configuration)
- Beholder (filewatcher)
- Camel-Snake-Kebab (naming conventions)
- Integrant (lifecycle)
- Malli (schema)
- Polylith (sw-architecture)
- Portal (beautiful, convenient debugging)
- Reitit (routing)
- Rum (html rendering)
- Flow-storm (omniscient debugging)

Using:

- hiccup (html templating)

And the following databases:

- XTDB V2 (immutable db w/ XTQL)

We additionally try to use the clojure ecosystem to its fullest with:

- Babashka (clojure scripting)

For the front-end, we try to use the hip old-is-new-again-back-to-basics tech, including:

- HTMX (modern ajax with memes)
- Tailwind (css class shortcuts)

And the following devops tools:

- Docker
- Docker-Compose
- Devcontainers (setup for vscode)
- Github Actions

## goals

In general the goal is to create a **_luxurious_** development/deployment experience which introduces a whole bunch of the clojure web ecosystem and debug tooling. For someone interested in playing with a bunch of popular modern clojure web libraries, this project could be an fun starting point.

Other goals include:

- Make it painfully easy to start developing a 'fully-loaded' clojure-centric web application which you have _full access_ to and _complete control_ over from the beginning.

- Make it painfully easy to locally build and deploy the same application, keeping the dev and prod environments as similar as possible.

- Provide _helpful pieces_ for setting up a 'production' environment, including a reverse proxy, https encryption, and automated deployment via github actions.

## non-goals

This is explorative work for the purpose of constructive joy. This is not best-practices from an authority in Clojure. This is a 'make it work, make it fun, make it better' project.

This is not:

- a 'make it perfect' project (though you are welcome to help with PRs and feedback!)
- production code
- minimalism

## prerequisites

To get the project up and running in the development environment, you will need:

- [docker](https://docs.docker.com/get-docker/)

## recommendations

- use vscode (otherwise the .devcontainer will need to be modified and the development workflow will need to be adjusted)
  - Remote Development extension pack
  - If you want to make this easily work with other IDEs, PRs are welcome :)

- OS:
  - If Windows:
    - for the love of all that is good and holy, just use [WSL2](https://learn.microsoft.com/en-us/windows/wsl/install). If you use Windows, [use Linux](https://learn.microsoft.com/en-us/windows/wsl/install).
  - If Other OS's:
    - I'm sure you'll be fine, but ymmv.

## setup

- clone repository
  - windows:
    - start WSL2
    - from WSL2, clone the repository
    - do _not_ clone to windows and mount the files in WSL2. This [will not work](https://blog.arkey.fr/2019/09/13/watchservice-and-bind-mount/) with the filewatcher.
  - Other OS's:
    - I'm sure you'll be fine

- development
  - vscode
    - Command Palette -> Dev Containers: Rebuild and Reopen in Container
      - the first time you do this, it will take a while
    - Command Palette -> Calva: Start a Project REPL and Connect (aka Jack-In) (+clj-storm,dev,test)
    - development/src/user.clj -> evaluate 'start-app'
  - other IDEs
    - ``devcontainer up --workspace-folder .``
    - follow something like [dev-containers-emacs](https://happihacking.com/blog/posts/2023/dev-containers-emacs/)
    - help me fill this out for _your_ preferred IDE!

- pre-production
  - rename '.env.deleteme' to '.env'
  - update .env to match your underlying platform (linux-arm64, linux-armv7, linux-x64 [for tailwind](https://github.com/tailwindlabs/tailwindcss/releases/tag/v3.4.4) you can find this with ``uname -m``)
  - ``docker compose up``

## 'production' (notes to get this working on a virtual private server (VPS) like Digital Ocean)

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

- if you have memory issues (your VPS, i cant help you personally), you may want to add some swap space?
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
  - in my case, i needed to change the underlying platform from linux-amd64 to linux-x64. this is used for [downloading tailwindcss](https://github.com/tailwindlabs/tailwindcss/releases/tag/v3.4.4>)

- now you will need to run the docker-compose file
  - ``docker compose up -d``
  - you may need to mess around with commands like ``docker compose down``, ``docker compose up --build`` and ``docker compose up --force-recreate``
  - you can clear out your old containers and volumes with:
  - ``docker system prune -a --volumes``

- now lets set up the ci/cd
  - on the server, copy your servers public key into the authorized_keys file
    - ``cat ~/.ssh/id_rsa.pub >> ~/.ssh/authorized_keys``
  - on your github repo, go to settings -> deploy keys
    - add the the necessary secrets (KEY USERNAME HOST)
    - KEY is the private key

## thanks

- A good amount of the current server-setup, filewatcher, time, and logging came from [Biff](https://github.com/jacobobryant/biff). Thanks Biff ([Jacob Bryant](https://github.com/jacobobryant))! If you want to build something with more batteries included and good documentation, check his project out.
- Also thank you to [Sean Corfield](https://github.com/seancorfield) for the initial [User Manager](https://github.com/seancorfield/usermanager-example).

## next steps

- update front-end to clojurescript
- actually do tests...
- shadow-cljs
- reagent
- re-frame
- electric clojure
- rama
- add security with buddy
- add mail
- add login
- add roles
- add websockets
- server creation automation with terraform
- server setup automation with ansible
