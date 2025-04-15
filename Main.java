public class Main {
    public static void main(String[] args) {
        // Pasta com os arquivos DNA
        String pasta = "src/arquivosDNA";

        // Pegar todos os arquivos dna-x.txt
        java.io.File dir = new java.io.File(pasta);
        java.io.File[] arquivos = dir.listFiles((d, nome) -> nome.matches("dna-\\d+\\.txt"));

        if (arquivos == null || arquivos.length == 0) {
            System.out.println("Sem arquivos dna-x.txt na pasta: " + pasta);
            return;
        }

        System.out.println("Achei " + arquivos.length + " arquivos.");


        GerenciadorDeArquivos gerenciadorTotal = new GerenciadorDeArquivos();

        // Threads para cada arquivo
        Thread[] threads = new Thread[arquivos.length];

        for (int i = 0; i < arquivos.length; i++) {
            final String arquivo = arquivos[i].getAbsolutePath();
            final String nome = arquivos[i].getName();

            threads[i] = new Thread(() -> {
                System.out.println("Processando: " + nome);

                GerenciadorDeArquivos gerenciador = new GerenciadorDeArquivos();
                gerenciador.LerFita(arquivo);


                System.out.println("\n----- RESUMO: " + nome + " -----");
                System.out.println("Total: " + gerenciador.lineNumber);
                System.out.println("Válidas: " + (gerenciador.lineNumber - gerenciador.erradas));
                System.out.println("Inválidas: " + gerenciador.erradas);
                System.out.println("Linhas com erro: " + gerenciador.linhasComErro);
                System.out.println("---------------------------");

                // Atualizar totais
                synchronized (gerenciadorTotal) {
                    gerenciadorTotal.erradas += gerenciador.erradas;
                    gerenciadorTotal.lineNumber += gerenciador.lineNumber;
                    gerenciadorTotal.linhasComErro.addAll(gerenciador.linhasComErro);
                }
            });

            threads[i].start();
        }


        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Mostrar resumo total
        System.out.println("\n----- RESUMO TOTAL -----");
        System.out.println("Total: " + gerenciadorTotal.lineNumber);
        System.out.println("Válidas: " + (gerenciadorTotal.lineNumber - gerenciadorTotal.erradas));
        System.out.println("Inválidas: " + gerenciadorTotal.erradas);
        System.out.println("Erros nas linhas: " + gerenciadorTotal.linhasComErro);
    }
}