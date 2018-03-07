package client;

import common.*;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class CommandHandler {
    private Socket sock;
    private ObjectOutputStream sOutput;
    private ObjectInputStream sInput;


    public CommandHandler() {}

    public static void main(String[] args) throws Throwable {
        int port = 3000;
        String serverAddress = "localhost";
        Socket sock = new Socket(serverAddress, port);
        ObjectOutputStream sOutput = new ObjectOutputStream(sock.getOutputStream());
        ObjectInputStream sInput = new ObjectInputStream(sock.getInputStream());
        Scanner s = new Scanner(System.in);
        while (true) {
            String str = s.nextLine();
            if ("register".equals(str)) {
                sOutput.writeObject(new ControlMessage(ControlMessage.Type.Register, "192.168.1.1,80,alexm"));
                RegisterReply r = (RegisterReply) sInput.readObject();
                System.out.println(r.id);
                sOutput.writeObject(new ControlMessage(ControlMessage.Type.JoinGroup,"distrib"));
                JoinGroupReply zz = (JoinGroupReply) sInput.readObject();
                System.out.println(zz.users);
                sOutput.writeObject(new ControlMessage(ControlMessage.Type.ListGroups, ""));
                ListGroupsReply x = (ListGroupsReply) sInput.readObject();
                System.out.println(x.groups);
//                sOutput.writeObject(new ControlMessage(ControlMessage.Type.ListMembers,"distrib"));
//                ListMembersReply z;
//                z = (ListMembersReply) sInput.readObject();
//                System.out.println(z.users);

            } else {
                System.out.println("Hello world");
            }
        }
    }
    private void beginConnection()throws Throwable{
        int port = 3000;
        String serverAddress = "localhost";
        sock = new Socket(serverAddress, port);
        sOutput = new ObjectOutputStream(sock.getOutputStream());
        sInput = new ObjectInputStream(sock.getInputStream());
    }
    public void execute (String command)throws Throwable{


            if(command.equals("r")) { // register user to tracker.
                this.beginConnection();
                sOutput.writeObject(new ControlMessage(ControlMessage.Type.Register, "192.168.1.1,80,alexm",-1));
                RegisterReply r = (RegisterReply) sInput.readObject();
                sock.close();
                System.out.println(r.id);
                Client.setClientId(r.id);
            }
            else if(command.equals("lg")){
                this.beginConnection();
                sOutput.writeObject(new ControlMessage(ControlMessage.Type.ListGroups, "", (int)Client.getClientId()));
                ListGroupsReply x = (ListGroupsReply) sInput.readObject();
                sock.close();
                System.out.println(x.groups);
            }
            else if(command.charAt(0) == 'l' && command.charAt(1) == 'm') {
                System.out.println("Hello man!");
                this.beginConnection();
                sOutput.writeObject(new ControlMessage(ControlMessage.Type.ListMembers,command.substring(3), (int)Client.getClientId()));
                ListMembersReply z;
                z = (ListMembersReply) sInput.readObject();
                sock.close();
                System.out.println(z.users);
            }
            else if(command.charAt(0) == 'j'){
                this.beginConnection();
                sOutput.writeObject(new ControlMessage(ControlMessage.Type.JoinGroup,command.substring(2), (int)Client.getClientId()));
                JoinGroupReply zz = (JoinGroupReply) sInput.readObject();
                sock.close();
                System.out.println(zz.users);
            }
            else if(command.charAt(0) == 'e'){
                this.beginConnection();
                sOutput.writeObject(new ControlMessage(ControlMessage.Type.ExitGroup,command.substring(2), (int)Client.getClientId()));
                sock.close();

            }
            else if(command.charAt(0)=='q'){
                this.beginConnection();
                sOutput.writeObject(new ControlMessage(ControlMessage.Type.Quit,"", (int)Client.getClientId()));
                sock.close();
            }
            else
                System.out.println("Unkown command. Type !h for the help menu");
    }

    public long registerClient() {
        return  1L;
    }

}

