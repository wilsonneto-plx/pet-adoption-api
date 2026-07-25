package adopet.api.service;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ImageStorageService {

    private final Path caminhoDiretorio = Paths.get("uploads");

    public String upload(MultipartFile imagem) {

        try {
            if (!Files.exists(caminhoDiretorio)) {
                Files.createDirectories(caminhoDiretorio);
            }

            String novoNome = this.gerarNovoNome(imagem.getOriginalFilename());

            Path caminhoCompletoDoArquivo = caminhoDiretorio.resolve(novoNome);

            imagem.transferTo(caminhoCompletoDoArquivo);

            return novoNome;

        } catch (IOException ex){
            throw new RuntimeException("Erro ao processar o upload da imagem", ex);
        }
    }

    public void apagar(String nomeArquivo) {

        try {
            if (nomeArquivo == null || nomeArquivo.isBlank()) {
                return;
            }

            Path caminhoCompletoDoArquivo = caminhoDiretorio.resolve(nomeArquivo);

            Files.deleteIfExists(caminhoCompletoDoArquivo);

        } catch (IOException ex) {
            System.out.println("Aviso: Não foi possível apagar a imagem antiga do disco: " + nomeArquivo );

        }
    }

    private String gerarNovoNome(String nomeOriginal)
    {
        String extensao = StringUtils.getFilenameExtension(nomeOriginal);

        return UUID.randomUUID() + "." + extensao;
    }

}
