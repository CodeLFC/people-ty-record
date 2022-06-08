package gaozhi.online.peoplety.record.entity;

import com.google.gson.Gson;
class RecordTest {

    public static void main(String[] args) {
        Record record = new Record(1,1,11,1,1,false,"2","2","2","2","2",1,"",false);

        System.out.println(new Gson().toJson(record));
        Favorite favorite = new Favorite(1,1,"test","test",1,false);
        System.out.println(new Gson().toJson(favorite));
        Favorite.Item item = new Favorite.Item(1,1,1,1);
        System.out.println(new Gson().toJson(item));
    }
}