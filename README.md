
# Assignment



Simple app to show some basic statistics on the chinook dataset. Database is Postgres, Spring boot for the backend, React for the frontend and deployed on Kubernetes.

### App
- Backend app exists to provide statistical data from the database based on externalized config files. App is intended to serve data which would require complex SQL queries, if this is not necessary, better off using OSS libraries such as PGRest etc. Sample config file, [Config](https://github.com/nithindv/assignment-2/blob/main/searcher/src/main/resources/chinook.yaml) 
- Frontend queries the backend and displays the data in the form of simple graphs or counters
- Deployment manifests templating is handled via Helm; uses a parent common chart to abstract out common repeatable items


## Demo
https://15.206.251.253.xip.io/chinook-dashboard/
It's hosted on AWS so the domain might expire in case the IP changes

### Project Structure
*Files / Folders*
 - deployment- All deployment related manifests
 - searcher- Consists of spring boot source code
 - searcher/Dockerfile - Dockerfile used for the build
 - searcher/start.sh - Shell script used to run the app, along with conditional environmental variables to support debugging. Quirks to support graceful shutdown in a docker env.
 - chinook-dashboard - Consists of the frontend source code, uses react, recharts, react-query, material-ui


### Build
- Docker build to package the app, [Dockerfile](https://github.com/nithindv/assignment-2/blob/master/searcher/Dockerfile)
- Multistage docker build
- Cache maven dependencies in builder image; light alpine JRE for runtime
- Similar approach used for the frontend app as well

      cd hello
      docker build -t nithindv/hello:v1 .
      docker push nithindv/hello:v1



### Deploy
- Details available [here](https://github.com/nithindv/assignment-2/blob/master/deployment/README.MD) 
 
      cd deployment/searcher
      helm dep update && helm template --set image.tag=latest --set global.domain=15.206.251.253.xip.io . > searcher-deployment.yaml
      kubectl apply -f searcher-deployment.yaml

      cd deployment/chinook-dashboard
      helm dep update && helm template --set image.tag=latest --set global.domain=15.206.251.253.xip.io . > dashboard-deployment.yaml
      kubectl apply -f dashboard-deployment.yaml      


## Dependencies

- Postgres

- Docker

- Helm

- Kubectl
