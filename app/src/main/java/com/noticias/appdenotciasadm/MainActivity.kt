package com.noticias.appdenotciasadm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.noticias.appdenotciasadm.databinding.ActivityMainBinding

// Aqui que entra a parte lógica do kotlin e a conexão com o banco
class MainActivity : AppCompatActivity() {
    // Com isso posso recuperar os elementos do layout(caixa de texto, botões) do ActivityMain.xml através da variável bindind.
    // Para ela funcionar é preciso ir no build.grade(app) e ativar viewBinding{enable=true}
    private lateinit var binding: ActivityMainBinding
    // Configurando variável para acessar banco FirebaseFirestore baseado em coleções
    private val db = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.hide()

        // Configurando ação do botão
        binding.btnPublicarNoticia.setOnClickListener{

            val titulo = binding.editTituloNoticia.text.toString()
            val noticia = binding.editNoticia.text.toString()
            val data = binding.editDataNoticia.text.toString()
            val autor = binding.editAutor.text.toString()

            // Verificar se há campos vazios e exibir mensagem na tela
            if (titulo.isEmpty() || noticia.isEmpty() || data.isEmpty() || autor.isEmpty()){
                Toast.makeText(this, "Preencha todos os campos!",Toast.LENGTH_SHORT).show() //Toast.LENGHT define o tempo que a mensagem ficará visivel
            }else{
                salvarNoticia(titulo, noticia, data, autor)
            }
        }

    }

    private fun salvarNoticia(titulo: String, noticia: String, data: String, autor: String) {

        val mapNoticias = hashMapOf( //hashmap com chaves e valores para usar no db
            "titulo" to titulo,
            "noticia" to noticia,
            "data" to data,
            "autor" to autor
        )

        db.collection("noticias")
            .document(""+titulo) // O document é a própria notícia. Noticia1, Noticia2...
            .set(mapNoticias).addOnCompleteListener { tarefa ->
                if (tarefa.isSuccessful) {
                    Toast.makeText(this, "Notícia publicada com sucesso!", Toast.LENGTH_SHORT).show()
                    limparCampos()
                    }
                }.addOnFailureListener() {
                    Toast.makeText(this, "Ocorreu um erro inesperado ao publicar.", Toast.LENGTH_SHORT).show()
                    }
          }

        private fun limparCampos(){

            binding.editTituloNoticia.setText("")
            binding.editNoticia.setText("")
            binding.editDataNoticia.setText("")
            binding.editAutor.setText("")
        }
}