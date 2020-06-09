# RepairAgency_spring

System Repair Agency (Car Repair Agency). The user can register, create order for
car repair, leave review on the work performed. The manager can accept the order, specify the price,
reject the order, comment the order, view all active orders or order history, view list of masters or customers.
The master can execute (edit status) the order accepted by the manager, view his active orders or
history of completed orders. An administrator can create, modify or delete master or manager accounts.
___
### You can view the system on the AWS server by following link:
##### http://carrepairagency.eu-central-1.elasticbeanstalk.com/home
___
### Instructions for installing and running on a local server (Tomcat):

1. Install the JDK at least version 8.
2. Install MySQl at least version 8 and create a 'root' user with the password 123456789.
3. Download the project (clone via git (https://github.com/ShulzhenkoA/RepairAgency_spring.git) or download
project and unzip).
4. Create a database and populate it by running the two SQL scripts in the project folder
(/RepairAgency_spring/src/main/resources/database/CreateRepairAgencyDatabase.sql and
/RepairAgency_spring/src/main/resources/database/PopulateDB.sql).
5. Open the 'Command Prompt' (console) from the project folder (RepairAgency_spring) or navigate in the 'Command Prompt' 
to the project folder (RepairAgency_spring).
6. > Execute in the 'Command Prompt' the command: mvn spring-boot:run
   
   Or

   > Execute in the 'Command Prompt' the commands (in order):
   >> mvn clean install  
   >> cd target  
   >> java -jar RepairAgency_spring-1.0.war
7. When the Spring boot is complete, go to http://localhost:8090/RepairAgency/home in your browser.
8. If the population of the database has been executed (item 4), then the system already has:
    > Administrator - email: admin@mail.com, password: Admin123;  
    Client - email: customer@mail.com, password: Customer123;  
    Master - email: master@mail.com, password: Master123;  
    Manager - email: manager@mail.com, password: Manager123.
9. To stop the system press ctrl+c+c in the 'Command Prompt' 
___
Система Ремонтне Агенство(Агенство з Ремонту Автомобілів). Користувач може зареєструватися, створити заявку на
ремонт автомобіля, залишити відгук про виконані роботи. Менеджер може прийняти заявку, вказавши ціну, або
відхилити заявку, вказавши причину, переглянути активні замовлення, історію замовлень, список майстрів або клієнтів.
Майстер може виконати (редагувати статус) прийняту менеджером заявку, переглянути свої активні замовлення або 
історію виконаних замовлень. Адміністратор може створити облікові записи майстра або менеджера, змінити їх або видалити.
___
### Переглянути систему на сервері AWS можна за посиланням:
##### http://carrepairagency.eu-central-1.elasticbeanstalk.com/home
___
### Інструкція з встановлення та запуску на локальному сервері (Tomcat):

1. Встановіть JDK не нижче 8 версії.
2. Встановіть MySQl не нижче 8 версії та створіть користувача 'root' з паролем 123456789.
3. Завантажте проект (зклонуйте через git (https://github.com/ShulzhenkoA/RepairAgency_spring.git) або завантажте
проект та розархівуйте).
4. Створіть базу даних та наповніть її, виконвши два SQL скріпти, що знаходяться в папці проекту 
(/RepairAgency_spring/src/main/resources/database/CreateRepairAgencyDatabase.sql та 
/RepairAgency_spring/src/main/resources/database/PopulateDB.sql).
5. Відкрийте 'Командний рядок'(консоль) з папки проекту (RepairAgency_spring) або перейдать у 'Командному рядку' в папку проету 
(RepairAgency_spring).
6. >Виконайте в 'Командному рядку' команду: mvn spring-boot:run  
   
   Або  

   >Виконайте в 'Командному рядку' по-порядку команди: 
   >>mvn clean install  
   >>cd target  
   >>java -jar RepairAgency_spring-1.0.war
7. Після завершення завантаження Spring перейдіть в браузері за посиланням http://localhost:8090/RepairAgency/home.
8. Якщо виконано наповнення бази даних (пункт 4), то в системі вже існують:  
    >Адміністратор - email: admin@mail.com, пароль: Admin123;  
    Клієнт - email: customer@mail.com, пароль: Customer123;  
    Майстер - email: master@mail.com, пароль: Master123;  
    Менеджер - email: manager@mail.com, пароль: Manager123.  
9. Щоб зупинити ситему в 'Командному рядку' натисніть ctrl+c+c

