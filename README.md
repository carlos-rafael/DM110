Trabalho Final DM110

- BD criado com o nome inventory

- Para criar a tabela address no banco:
create table poller(
ips varchar(50) not null,
status bool default 'false',
constraint pk_host primary key (ips)
);

Wildfly queue configuration:
Name: IpAddressQueue
JNDI Names: java:/jms/queue/IpAddressQueue

