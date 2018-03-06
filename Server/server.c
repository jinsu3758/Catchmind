#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <sys/socket.h>
#include <arpa/inet.h>
#include <pthread.h>

#define BUF_SIZE 150
#define MAX_CLNT 4
#define WIN 10
#define MAX_XY 20
#define NAME_SIZE 20


void * handle_clnt(void * arg);
void * handle_udp(void * arg);
void send_msg(char * msg,int len);
void error(char * msg);

int clnt_cnt=0;
int user_cnt=1;
int clnt_socks[MAX_CLNT];
int udp_user[MAX_CLNT];
int Turn=0;
int udp[MAX_CLNT];
struct sockaddr_in udp_addr[MAX_CLNT];
struct sockaddr_in udp_serv_addr;
//char answer[50][50] = {"사자","호랑이","뱀","라면","고기","매미","곰","공책","연필","마라통"};
char answer[50][50]={"oh","my","god","hell"};
typedef struct _User{
	char name[20];
	int  turn;
	int  cnt;
}User;
User user[4];
char xy[10];
char udp_name[20];
int n=0;

pthread_mutex_t mutx;

int main(int argc,char *argv[])
{
	int serv_sock,clnt_sock;
	int udp_serv,udp_len,udp_clnt;

	struct sockaddr_in serv_adr,clnt_adr;
	int clnt_adr_size,udp_size;


	char name[20];


	pthread_t id;
	pthread_t udp;

	if(argc!=2)
	{
		printf("Usage : %s <port>\n",argv[0]);
		exit(1);
	}
	//printf("1111111111111\n");

	pthread_mutex_init(&mutx,NULL);
	serv_sock=socket(PF_INET,SOCK_STREAM,0);
	udp_serv=socket(PF_INET,SOCK_DGRAM,0);
	//printf("2222222222222\n");

	memset(&serv_adr,0,sizeof(serv_adr));
	serv_adr.sin_family=AF_INET;
	serv_adr.sin_addr.s_addr=htonl(INADDR_ANY);
	serv_adr.sin_port=htons(atoi(argv[1]));
	//printf("333333333333\n");

	//	memset(&udp_serv_addr,0,sizeof(udp_serv_addr));
	//	udp_serv_addr.sin_family=AF_INET;
	//	udp_serv_addr.sin_addr.s_addr=htonl(INADDR_ANY);
	//	udp_serv_addr.sin_port=htons(atoi(argv[1]));

	//printf("444444444444444\n");

	if(bind(serv_sock,(struct sockaddr*)&serv_adr,sizeof(serv_adr))==-1)
	{
		error("tcp bind error");
	}
	//printf("5555555555555\n");

//	if(bind(udp_serv,(struct sockaddr*)&udp_serv_addr,sizeof(udp_serv_addr))==-1)
//	{
//		error("udp bind error");
//	}
	if(listen(serv_sock,5)==-1)
	{
		error("listen error");
	}

	char first[20]="{";
	strcat(first,answer[1]);
	strcat(first,"\r\n");

	while(1)
	{
		//printf("777777777\n");
		clnt_adr_size=sizeof(clnt_adr);
		clnt_sock=accept(serv_sock,(struct sockaddr*)&clnt_adr,&clnt_adr_size);
		//printf("Client ip : %s",clnt_adr.sin_addr);
		char a[20];
		int ab;
		pthread_mutex_lock(&mutx);
		ab=	read(clnt_sock,a,20);
		strncpy(name,a,strlen(a)-1);
		name[strlen(name)-1]='\0';
	//	printf("%d eee\n",ab);
	//	printf("%s@@@@@@\n",name);
		//	printf("Client ip : %d : %s\n++++\n",strlen(name),name);
		strcpy(user[clnt_cnt].name,name);
		user[clnt_cnt].cnt=0;
		clnt_socks[clnt_cnt++]=clnt_sock;
//		if(clnt_cnt==2)
//		{
//			write(clnt_socks[0],first,20);
//		}
			
		//	user[clnt_cnt++].turn=clnt_cnt;
		//	printf("clnt cnt : %d\n",clnt_cnt);
		//printf("8888888888888888888\n");
		//udp_size = sizeof(udp_addr[clnt_cnt]);
		//recvfrom(udp_serv,udp_name , NAME_SIZE, 0, (struct sockaddr*)&udp_addr[clnt_cnt++],
		//	&udp_size);
		pthread_mutex_unlock(&mutx);
		//printf("8888888888888888888\n");


		if(name!=NULL)
		{
			pthread_create(&id,NULL,handle_clnt,(void*)&clnt_sock);
		}
		//if(clnt_cnt==4)
		//{
		//pthread_create(&udp, NULL, handle_udp, (void*)&udp_serv);
		//}
		pthread_detach(id);
		//pthread_detach(udp);
		//printf("9999999999999999999\n");


		printf("Connected client : %s \n",name);


	}
	close(serv_sock);
	close(udp_serv);
	return 0;
}

void * handle_clnt(void * arg)
{
	int clnt_sock=*((int*)arg);
	int str_len=0;
	char msg[BUF_SIZE];
	char msg_r[BUF_SIZE];
	char name_msg[BUF_SIZE];
	char *your_name;
	int an=0;
	while((str_len=read(clnt_sock,msg,sizeof(msg)))!=0)
	{
		//		printf("From Client : %s\n+++++",msg);
		//		printf("send\n")a
	//	pthread_mutex_lock(&mutx);
		strcpy(msg_r,msg);
		char *msg2=strtok(msg_r," : ");
		char *msg3=strtok(NULL," : ");
		//		printf("%s#####\n",msg3);
		strcpy(name_msg,msg2);
		//	if(msg3!=NULL)
		//		msg3[strlen(msg3)-1]='\0';
		if(name_msg!=NULL)
		{
			your_name=strtok(name_msg,"[");
			your_name=strtok(your_name,"]");
			strcat(your_name,"\n");
		}
		//		printf("%s!!!!!\n",your_name);
		//		printf("%s@@@@\n",msg3);
		if(msg3!=NULL && msg[0]!='i')
		{
			for(int i=0; i<4; i++)
			{
				if(!strcmp(msg3,answer[i]))
				{
				//	printf("정답!\n");
					for(int j=0; j<clnt_cnt; j++)
					{
					//	printf("%s is name\n",user[j].name);
					//	printf("%s is yourname\n",your_name);
						for(int k=0; k<strlen(your_name);k++)
						{
					//	if(!strcmp(your_name,user[j].name))
							if(your_name[k]==user[j].name[k])
						{
							//	printf("정답이에요\n");
						//	user[j].cnt++;
							an++;
					//		printf("%s %d !@!\n",user[j].name,j);
						}
						}
						if(an>=3)
						{

							if(Turn==clnt_cnt-1)
							{
								Turn=0;
							}
							else
							{
								Turn++;
							}
							user[j].cnt++;
							an=0;
							n=rand()%4;
							break;
						}
					}
					break;
				}
			}
		}
//		pthread_mutex_unlock(&mutx);

		//		printf("To : %s\n",msg);
		send_msg(msg,str_len);
	}

	//	pthread_mutex_unlock(&mutx);
	/*pthread_mutex_lock(&mutx);
	  for(int i=0;i<clnt_cnt;i++)
	  {
	  if(clnt_sock==clnt_socks[i])
	  {
	  while(i++<clnt_cnt-1)
	  {
	  clnt_socks[i]=clnt_socks[i+1];
	  }
	  break;
	  }
	  }

	  clnt_cnt--;
	  pthread_mutex_unlock(&mutx);*/
	close(clnt_sock);
	return NULL ;
}

void send_msg(char * msg,int len)
{
	int i;
	char msg2[BUF_SIZE];
	pthread_mutex_lock(&mutx);
	char end[BUF_SIZE]="end";

	//	printf("%d\n",clnt_cnt);
	//	printf("send\n");
	if(msg[0]!='i')
	{
		printf("여긴채팅\n");
	for(i=0; i<clnt_cnt; i++)
	{
		//	printf("write\n");
		//strcat(msg,"\r\n");
		strcpy(msg2,msg);
		strcat(msg2,";");
		strcat(msg2,user[Turn].name);
		strcat(msg2,";");
		strcat(msg2,answer[n]);
		printf("%s@@@@@@\n",answer[n]);
		strcat(msg2,"\n");
		printf("%s!!!\n",msg);
		printf("n : %d###\n",n);
		printf("%s@@@\n",msg2);
		int k=write(clnt_socks[i],msg2,strlen(msg2));
		for(int j=0; j<clnt_cnt; j++)
		{
			if(user[j].cnt==5)
			{
				strcat(end," ");
				strcat(end,user[j].name);
				strcat(end," win!!!!");
				for(int k=0; k<clnt_cnt;k++)
				{
				write(clnt_socks[k],end,strlen(end));
				}
				exit(0);
			}
		}
	}
	}
	else
	{
		for(int j=0;j<clnt_cnt;j++)
		{
			write(clnt_socks[j],msg,len);
		}
	}


	pthread_mutex_unlock(&mutx);
}

void * handle_udp(void * arg)
{
	int udp_sock = *((int*)arg);
	char next[1]="0";
	int udp_size;
	int size;
	int udp_len=-1;
	int cnt=0;
	int turn=10;
	struct sockaddr_in udp;

	//	pthread_mutex_lock(&mutx);
	//udp_size = sizeof(udp_addr[user[clnt_cnt-1].turn]);
	//recvfrom(udp_sock, user_turn, 1, 0, (struct sockaddr*)&udp_addr[clnt_cnt-1],
	//&udp_size);
	//udp_user[i] = i + 1;
	//sprintf(us, "%d", udp_user[i]);
	//sendto(udp_sock, us, 1, 0, (struct sockaddr*)&udp_addr[i],
	//	sizeof(udp_addr[i]));	
	while (1)
	{
		for(int i=0; i<4; i++)
		{
			udp_size = sizeof(udp_addr[i]);
			sendto(udp_sock,user[Turn].name,20 ,0, (struct sockaddr*)&udp_addr[i],
					sizeof(udp_addr[i]));
		}

		size=sizeof(udp);
		udp_len = recvfrom(udp_sock, xy, MAX_XY, 0, (struct sockaddr*)&udp,
				&size);
		if (udp_len >= 0)
		{
			for (int i = 0; i < 4; i++)
			{
				//		if (i != turn)
				//		{
				sendto(udp_sock,user[Turn].name ,NAME_SIZE , 0, (struct sockaddr*)&udp_addr[i],
						sizeof(udp_addr[i]));
				//		}
			}
		}


	}
	return NULL;
	//	pthread_mutex_unlock(&mutx);
}
void error(char *msg)
{
	fputs(msg,stderr);
	fputc('\n',stderr);
	exit(1);
}








