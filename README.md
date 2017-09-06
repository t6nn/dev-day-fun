# Setup
1. Enable docker swarm mode (`docker swarm init`)
2. Build the docker image for Jenkins (`server/build_image.sh`)

# Starting the solution
1. Set the admin user/pass for Jenkins (see [deploy_stack.sh](server/deploy_stack.sh) for an example).
2. Deploy the Jenkins stack (`server/deploy_stack.sh`)
3. Once Jenkins starts, install Ant there (not done automatically at the moment). Manage Jenkins -> Tools -> Ant installation. Name it "ant-main".
4. Start the frontend (CodeCompetitionFeApplication).
