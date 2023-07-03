package com.challenge.conexa.service;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.challenge.conexa.models.dto.AppointmentDTO;
import com.challenge.conexa.models.entity.Appointment;
import com.challenge.conexa.models.entity.Patient;
import com.challenge.conexa.models.entity.User;
import com.challenge.conexa.repository.AppointmentRepository;
import com.challenge.conexa.repository.UserRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final PatientService patientService;
    private final ProfessionalService professionalService;

    public Appointment save(Appointment appointment) throws Exception {
        if (appointmentRepository.existsByDateTimeAndProfessionalId(
                appointment.getDateTime(), appointment.getProfessional().getId()
            )
        ) {
            throw new Exception("ProfessionalAlreadyInUseException");
        }

        Patient patient = patientService.findById(appointment.getPatient().getId()).get();
        appointment.setProfessional(professionalService.findById(appointment.getProfessional().getId()).get());
        appointment.setPatient(patient);

        
        patientService.incrementPatientAppointments(patient.getId());

        return appointmentRepository.save(appointment);
    }

    public List<Appointment> findAll() {
        return appointmentRepository.findAll();
    }

    public void deleteById(Long id) {
        appointmentRepository.deleteById(id);
    }

    

}