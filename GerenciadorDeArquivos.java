import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GerenciadorDeArquivos {
    public int erradas = 0;
    public List<Integer> linhasComErro = new ArrayList<>();
    public int lineNumber = 0;

    public String LerFita(String arquivo) {
        StringBuilder result = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
            String line;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (verificarLinha(line))
                    result.append(ConverterFita(line)).append("\n");
                else {
                    result.append("****FITA INVALIDA - Linha ").append(lineNumber).append(": ").append(line).append("\n");
                    erradas++;
                    linhasComErro.add(lineNumber);
                }
            }
            novoArquivo(String.valueOf(result), arquivo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    public boolean verificarLinha(String linha) {
        for(String character : linha.split("")) {
            if (!character.equals("A") && !character.equals("T") && !character.equals("G") && !character.equals("C"))
                return false;
        }
        return true;
    }

    public void novoArquivo(String line, String nomeArquivo) {
        try {
            FileWriter myWriter = new FileWriter(nomeArquivo + "comp");
            myWriter.write(line);
            myWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String ConverterFita(String line) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            switch (c) {
                case 'A': result.append('T'); break;
                case 'T': result.append('A'); break;
                case 'G': result.append('C'); break;
                case 'C': result.append('G'); break;
                default: result.append(c); break;
            }
        }

        return result.reverse().toString();
    }

    public List<Integer> getLinhasComErro() {
        return linhasComErro;
    }
}