package com.example.easynotes.controller;

import com.example.easynotes.exception.ResourceNotFoundException;
import com.example.easynotes.legacysystems.LegacySystemsConnector;
import com.example.easynotes.model.Note;
import com.example.easynotes.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/api")
public class NoteController {

    @Autowired
    NoteRepository noteRepository;

    // Get All Notes
    @GetMapping("/notes")
    public List<Note> getAllNotes() {
        return noteRepository.findAll();
    }

    // Create a new Note
    @PostMapping("/notes")
    public Note createNote(@Valid @RequestBody Note note) {
    	Random rand = new Random();
        int referenceKey = rand.nextInt((100000 - 1) + 1) + 1;
        
        LegacySystemsConnector.connectAndTrigger(referenceKey, note.getTitle());
    	
        return noteRepository.save(note);
    }
    
    // Get a Single Note
    @GetMapping("/notes/{id}")
    public Note getNoteById(@PathVariable(value = "id") Long noteId) {
        return noteRepository.findById(noteId)
                .orElseThrow(() -> new ResourceNotFoundException("Note", "id", noteId));
    }
    
    // Update a Note
    @PutMapping("/notes/{id}")
    public Note updateNote(@PathVariable(value = "id") Long noteId,
                                            @Valid @RequestBody Note noteDetails) {

        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new ResourceNotFoundException("Note", "id", noteId));

        note.setTitle(noteDetails.getTitle());
        note.setContent(noteDetails.getContent());

        Note updatedNote = noteRepository.save(note);
        return updatedNote;
    }
    
    // Delete a Note
    @DeleteMapping("/notes/{id}")
    public ResponseEntity<?> deleteNote(@PathVariable(value = "id") Long noteId) {
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new ResourceNotFoundException("Note", "id", noteId));

        noteRepository.delete(note);

        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/notes/java_error")
    public Note throwException() throws Exception {
        try {
            throw new Exception("Forced Exception");
        } catch (Exception e) {
        	throw new ResourceNotFoundException("Note", "", null);
        }
    }
    
    @GetMapping("/notes/slow/{delay}")
    public String slowRequest(@PathVariable("delay") int delay) throws Exception {
        try {
            for(int i = 0; i < delay; i++) Thread.sleep(1000);
        } catch(InterruptedException ex) {
        }

        return "Completed transaction with " + delay + " seconds delay";
    }

    @GetMapping("/notes/sql_error")
    public Note throwSqlException() throws Exception {
        try {
            throw new Exception("SQL Exception");
        } catch (Exception e) {
        	throw e;
        }
    }
}