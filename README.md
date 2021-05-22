# cowin-spring-web-based


### CONSTRAINTS CONSIDERED
        1. Valid age 18, 45
        2. Date param must be in the format dd-mm-yyyy
   

### Operations supported:
        fetchAvailableVaccine : fetches the available slots with total vaccine availables for the given age and time period.

### API CONTRACT:

http://{{host}}:{{port}}/available?age={{age}}&date={{date}}
for reference : http://localhost:8080/available?age=18&date=22-05-2021

### INSTALLATION STEPS (Make sure docker is installed)
[Docker Image](https://hub.docker.com/r/amanjadon54/cowin) 
1. Get the docker image from above link or pull directly using:
  
           docker pull amanjadon54/cowin:1.0.0
           
2. Execute the command
          
           docker run -p 8080:8080 amanjadon54/cowin:1.0.0
       
If Docker is not installed, You can run it directly by cloning the repository and performing:\
        
        1. cd <project_root_directory>
        2. mvn clean install
        3. java -jar target/cowin-1.0-SNAPSHOT.jar


