package com.example.GestorMarcaYModelo.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.GestorMarcaYModelo.model.Marca;
import com.example.GestorMarcaYModelo.model.Modelo;
import com.example.GestorMarcaYModelo.repository.MarcaRepository;
import com.example.GestorMarcaYModelo.repository.ModeloRepository;

@Configuration
public class LoadDatabase {

    @Bean
    CommandLineRunner initDatabase(MarcaRepository marcaRepo, ModeloRepository modeloRepo) {
        return args -> {
            if (marcaRepo.count() == 0 && modeloRepo.count() == 0) {

                // Crear marcas
                Marca johnDeere = new Marca();
                johnDeere.setNombre("John Deere");
                marcaRepo.save(johnDeere);

                Marca newHolland = new Marca();
                newHolland.setNombre("New Holland");
                marcaRepo.save(newHolland);

                Marca caseIH = new Marca();
                caseIH.setNombre("Case IH");
                marcaRepo.save(caseIH);

                // Crear modelos para John Deere
                Modelo jd7200 = new Modelo();
                jd7200.setNombre("7200");
                jd7200.setMarca(johnDeere);
                modeloRepo.save(jd7200);

                Modelo jd5055 = new Modelo();
                jd5055.setNombre("5055E");
                jd5055.setMarca(johnDeere);
                modeloRepo.save(jd5055);

                Modelo jdS680 = new Modelo();
                jdS680.setNombre("S680");
                jdS680.setMarca(johnDeere);
                modeloRepo.save(jdS680);

                Modelo jdR4045 = new Modelo();
                jdR4045.setNombre("R4045");
                jdR4045.setMarca(johnDeere);
                modeloRepo.save(jdR4045);

                // Crear modelos para New Holland
                Modelo nhT6050 = new Modelo();
                nhT6050.setNombre("T6050");
                nhT6050.setMarca(newHolland);
                modeloRepo.save(nhT6050);

                Modelo nhCR790 = new Modelo();
                nhCR790.setNombre("CR7.90");
                nhCR790.setMarca(newHolland);
                modeloRepo.save(nhCR790);

                // Crear modelos para Case IH
                Modelo casePuma185 = new Modelo();
                casePuma185.setNombre("Puma 185");
                casePuma185.setMarca(caseIH);
                modeloRepo.save(casePuma185);

                Modelo case1230 = new Modelo();
                case1230.setNombre("1230");
                case1230.setMarca(caseIH);
                modeloRepo.save(case1230);


                System.out.println("🚜 Marcas y modelos precargados correctamente");
            } else {
                System.out.println("📦 Ya existen datos en la base. No se cargó nada nuevo.");
            }
        };
    }
   }