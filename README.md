# RepairAgency_spring

__System Repair Agency (Car Repair Agency)__. The user can register, create order for
car repair, leave review on the work performed. The manager can accept the order, specify the price,
reject the order, comment the order, view all active orders or order history, view list of masters or customers.
The master can execute (edit status) the order accepted by the manager, view his active orders or
history of completed orders. An administrator can create, modify or delete master or manager accounts.
___
### You can view the system on the AWS server by following link:
##### http://carrepairagency.eu-central-1.elasticbeanstalk.com/home
___
### Instructions for installing and running on a local server (Tomcat):

1. Install the JDK at least version 8. Set environment variables for Java.
2. Install MySQl at least version 8 and create a __root__ user with the password: __123456789__.
3. Download the project (clone via git (_https://github.com/ShulzhenkoA/RepairAgency_spring.git_) or download
project and unzip).
4. Create a database and populate it by running two SQL scripts in the project folder
(/RepairAgency_spring/src/main/resources/database/__CreateRepairAgencyDatabase.sql__ and
/RepairAgency_spring/src/main/resources/database/__PopulateDB.sql__).
5. Open the _Command Prompt_ (console) from the project folder (_RepairAgency_spring_) or navigate in the _Command Prompt_ 
to the project folder (_/RepairAgency_spring_) where contains __pom.xml__.
6. > Execute in the _Command Prompt_ the command: __mvnw spring-boot:run__
   
   Or

   > Execute in the _Command Prompt_ the commands (in order):
   >> __mvnw clean install__  
   >> __cd target__  
   >> __java -jar RepairAgency_spring-1.0.war__
7. When the Spring boot is complete, go to __http://localhost:8090/CarRepairAgency/home__ in your browser.
8. If the population of the database has been executed (item 4), then the system already has:
    > Administrator - email: __admin@mail.com__, password: __Admin123__;  
    Client - email: __customer@mail.com__, password: __Customer123__;  
    Master - email: __master@mail.com__, password: __Master123__;  
    Manager - email: __manager@mail.com__, password: __Manager123__.
9. To stop the system press __ctrl+c+c__ in the __Command Prompt__. 
___
___
___
__Система Ремонтне Агенство(Агенство з Ремонту Автомобілів)__. Користувач може зареєструватися, створити заявку на
ремонт автомобіля, залишити відгук про виконані роботи. Менеджер може прийняти заявку, вказавши ціну, або
відхилити заявку, вказавши причину, переглянути активні замовлення, історію замовлень, список майстрів або клієнтів.
Майстер може виконати (редагувати статус) прийняту менеджером заявку, переглянути свої активні замовлення або 
історію виконаних замовлень. Адміністратор може створити облікові записи майстра або менеджера, змінити їх або видалити.
___
### Переглянути систему на сервері AWS можна за посиланням:
##### http://carrepairagency.eu-central-1.elasticbeanstalk.com/home
___
### Інструкція з встановлення та запуску на локальному сервері (Tomcat):

1. Встановіть JDK не нижче 8 версії. Встановіть змінні середовища для Java.
2. Встановіть MySQl не нижче 8 версії та створіть користувача 'root' з паролем 123456789.
3. Завантажте проект (зклонуйте через git (_https://github.com/ShulzhenkoA/RepairAgency_spring.git_) або завантажте
проект та розархівуйте).
4. Створіть базу даних та наповніть її, виконавши два SQL скріпти, що знаходяться в папці проекту 
(/RepairAgency_spring/src/main/resources/database/__CreateRepairAgencyDatabase.sql__ та 
/RepairAgency_spring/src/main/resources/database/__PopulateDB.sql__).
5. Відкрийте _Командний рядок_ (консоль) з папки проекту (_RepairAgency_spring_) або перейдіть у _Командному рядку_ 
до папки проету (_/RepairAgency_spring_), де міститься __pom.xml__.
6. >Виконайте в _Командному рядку_ команду: __mvnw spring-boot:run__  
   
   Або  

   >Виконайте в _Командному рядку_ по-порядку команди: 
   >>__mvnw clean install__  
   >>__cd target__  
   >>__java -jar RepairAgency_spring-1.0.war__
7. Після завершення завантаження Spring перейдіть в браузері за посиланням __http://localhost:8090/CarRepairAgency/home__.
8. Якщо виконано наповнення бази даних (пункт 4), то в системі вже існують:  
    >Адміністратор - email: __admin@mail.com__, пароль: __Admin123__;  
    Клієнт - email: __customer@mail.com__, пароль: __Customer123__;  
    Майстер - email: __master@mail.com__, пароль: __Master123__;  
    Менеджер - email: __manager@mail.com__, пароль: __Manager123__.  
9. Щоб зупинити ситему в _Командному рядку_ натисніть __ctrl+c+c__

