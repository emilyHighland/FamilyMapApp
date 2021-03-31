

public class Server {
    public static void main(String[] Args){
        FamilyMapServer familyMap = new FamilyMapServer();
        int port = Integer.parseInt(Args[0]);
        familyMap.main(port);
    }
}
