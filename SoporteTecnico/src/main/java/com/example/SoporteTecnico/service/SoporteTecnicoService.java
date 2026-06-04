package com.example.SoporteTecnico.service;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.SoporteTecnico.model.Ticket;
import com.example.SoporteTecnico.model.Soporte;
import com.example.SoporteTecnico.model.TipoSoporte;
import com.example.SoporteTecnico.repository.SoporteRepository;
import com.example.SoporteTecnico.repository.TicketRepository;
import com.example.SoporteTecnico.repository.TipoSoporteRepository;
import com.example.SoporteTecnico.webclient.UsuarioUser;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class SoporteTecnicoService {

    @Autowired
    private SoporteRepository soporteRepository;
    
    @Autowired
    private TicketRepository ticketRepository;
    
    @Autowired
    private TipoSoporteRepository tipoSoporteRepository;
    
    @Autowired
    private UsuarioUser usuarioUser;
    
    // Métodos para Tickets (ya implementados)
    public List<Ticket> getTickets(){
        return ticketRepository.findAll();
    }
    
    public Ticket saveTicket(Ticket tk){
        Map<String,Object> usuario = usuarioUser.getUsuarioPorId(tk.getIdUsuario());
        if(usuario == null || usuario.isEmpty()){
            throw new RuntimeException("usuario no encontrado");
        }
        return ticketRepository.save(tk);
    }
    
    public Ticket getTicket(Integer id){
        return ticketRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Ticket no encontrado " + id));
    }
    
    public boolean deleteTicketById(Integer id) {
        if (ticketRepository.existsById(id)) {
            ticketRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
    
    public Ticket actualizarTicket(Integer id, Ticket nuevoTicket) {
        if (nuevoTicket == null) {
            throw new IllegalArgumentException("El ticket enviado es nulo");
        }
        Integer idUsuario = nuevoTicket.getIdUsuario();
        if (idUsuario == null || idUsuario <= 0) {
            throw new IllegalArgumentException("El ID de usuario no es válido");
        }
        Map<String, Object> usuario = usuarioUser.getUsuarioPorId(idUsuario);
        if (usuario == null || usuario.isEmpty()) {
            throw new EntityNotFoundException("El usuario con ID " + idUsuario + " no existe");
        }
        Ticket existente = ticketRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Ticket con ID " + id + " no encontrado"));
            
        if (nuevoTicket.getFecha_inicio() == null) {
            throw new IllegalArgumentException("La fecha de inicio es obligatoria");
        }
            
        existente.setFecha_inicio(nuevoTicket.getFecha_inicio());
        existente.setFecha_cierre(nuevoTicket.getFecha_cierre());
        existente.setDescripcion(nuevoTicket.getDescripcion());
        existente.setIdUsuario(idUsuario);
    
        return ticketRepository.save(existente);
    }
    
    // Nuevo método para listar tickets por usuario
    public List<Ticket> getTicketsByUsuarioId(Integer usuarioId) {
        return ticketRepository.findByIdUsuario(usuarioId);
    }
    
    // Métodos para Soporte (CRUD)
    public Soporte createSoporte(Soporte soporte) {
        return soporteRepository.save(soporte);
    }
    
    public Soporte updateSoporte(Integer id, Soporte newSoporte) {
        Soporte existente = soporteRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Soporte no encontrado con id " + id));
        existente.setFecha_soporte(newSoporte.getFecha_soporte());
        existente.setObservacion(newSoporte.getObservacion());
        if(newSoporte.getTicket() != null){
            existente.setTicket(newSoporte.getTicket());
        }
        return soporteRepository.save(existente);
    }
    
    public boolean deleteSoporteById(Integer id) {
        if (soporteRepository.existsById(id)) {
            soporteRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    public List<Soporte> getSoportes() {
        return soporteRepository.findAll();
    }
    
    // Métodos para TipoSoporte (CRUD)
    public TipoSoporte createTipoSoporte(TipoSoporte ts) {
        return tipoSoporteRepository.save(ts);
    }
    
    public TipoSoporte updateTipoSoporte(Integer id, TipoSoporte newTipo) {
        TipoSoporte existente = tipoSoporteRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("TipoSoporte no encontrado con id " + id));
        existente.setNombre(newTipo.getNombre());
        if(newTipo.getTicket() != null){
            existente.setTicket(newTipo.getTicket());
        }
        return tipoSoporteRepository.save(existente);
    }
    
    public boolean deleteTipoSoporteById(Integer id) {
        if (tipoSoporteRepository.existsById(id)) {
            tipoSoporteRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    public List<TipoSoporte> getTipoSoportes() {
        return tipoSoporteRepository.findAll();
    }
}
