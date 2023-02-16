package src.repository.file;

import src.domain.Entity;
import src.domain.Friendship;
import src.domain.User;
import src.domain.validators.Validator;
import src.repository.memory.InMemoryRepository;

import java.io.*;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractFileRepository<ID, E extends Entity<ID>> extends InMemoryRepository<ID,E> {

    String fileName;

    public AbstractFileRepository(String fileName) {
        this.fileName = fileName;
        loadData();
    }

    public void loadData(){
        try(BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while((line=br.readLine())!=null){
                String[] attr = line.split(",");
                E e = extractEntity(attr);
                super.save(e);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public E delete(ID id) {
        removeFromFile(id);
        return super.delete(id);
    }


    private void removeFromFile(ID id) {
        String tempFile = "temp.txt";
        File oldFIle = new File(fileName);
        File newFile = new File(tempFile);

        String currentLine;
        String str = this.findOne(id).getId().toString();

        try {
            FileWriter fw = new FileWriter(tempFile, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);

            FileReader fr = new FileReader(fileName);
            BufferedReader br = new BufferedReader(fr);

            while ((currentLine = br.readLine()) != null) {
                if (!currentLine.startsWith(str)) {
                    pw.println(currentLine);
                }
            }

            pw.flush();
            pw.close();
            fr.close();
            br.close();
            bw.close();
            fw.close();

            oldFIle.delete();
            File dump = new File(fileName);
            newFile.renameTo(dump);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public abstract E extractEntity(String[] attributes);

    protected abstract String createEntityAsString(E entity);

    @Override
    public E save(E entity){
        E e=super.save(entity);
        if (e==null)
        {
            writeToFile(entity);
        }
        return e;

    }



    protected void writeToFile(E entity){
        try (BufferedWriter bW = new BufferedWriter(new FileWriter(fileName,true))) {
            bW.write(createEntityAsString(entity));
            bW.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
