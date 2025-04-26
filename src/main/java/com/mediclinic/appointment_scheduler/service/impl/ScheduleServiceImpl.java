package com.mediclinic.appointment_scheduler.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.mediclinic.appointment_scheduler.domain.Doctor;
import com.mediclinic.appointment_scheduler.domain.Schedule;
import com.mediclinic.appointment_scheduler.domain.response.ResPaginationDTO;
import com.mediclinic.appointment_scheduler.domain.response.schedule.ResScheduleDTO;
import com.mediclinic.appointment_scheduler.repository.DoctorRepository;
import com.mediclinic.appointment_scheduler.repository.ScheduleRepository;
import com.mediclinic.appointment_scheduler.service.ScheduleService;
import com.mediclinic.appointment_scheduler.util.error.IdInvalidException;
import com.mediclinic.appointment_scheduler.util.error.ResourceAlreadyExistsException;
import com.mediclinic.appointment_scheduler.util.error.ResourceNotFoundException;

@Service
public class ScheduleServiceImpl implements ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final DoctorRepository doctorRepository;

    public ScheduleServiceImpl(ScheduleRepository scheduleRepository, DoctorRepository doctorRepository) {
        this.scheduleRepository = scheduleRepository;
        this.doctorRepository = doctorRepository;
    }

    @Override
    public ResScheduleDTO createSchedule(Schedule schedulePM) {
        if (!this.scheduleRepository.findAll().isEmpty()) {
            if (!this.isExistsTimeSlotBuildQuery(schedulePM))
                schedulePM.setTimeSlot(schedulePM.getTimeSlot());
            else
                throw new ResourceAlreadyExistsException("Lịch đã tồn tại");
        }
        Schedule scheduleDB = this.scheduleRepository.save(schedulePM);
        return ResScheduleDTO.mapEntityScheduleToDTO(scheduleDB);
    }

    @Override
    public ResScheduleDTO updateSchedule(Schedule schedulePM) {
        if (schedulePM.getId() < 1) {
            throw new IdInvalidException("Id không tồn tại");
        }
        Schedule scheduleDB = this.scheduleRepository.findById(schedulePM.getId())
                .orElseThrow(() -> new ResourceNotFoundException("không tồn tại lịch làm việc này"));
        scheduleDB.setWorkDate(schedulePM.getWorkDate());
        if (!isExistsTimeSlotBuildQuery(schedulePM))
            scheduleDB.setTimeSlot(schedulePM.getTimeSlot());
        else
            throw new ResourceAlreadyExistsException("Lịch đã tồn tại");
        scheduleDB.setStatus(schedulePM.getStatus());
        this.scheduleRepository.save(scheduleDB);
        return ResScheduleDTO.mapEntityScheduleToDTO(scheduleDB);
    }

    @Override
    public void deleteSchedule(Long id) {
        if (id < 1) {
            throw new IdInvalidException("Id không tồn tại");
        }
        Schedule scheduleDB = this.scheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("không tồn tại lịch làm việc này"));
        this.scheduleRepository.delete(scheduleDB);
    }

    @Override
    public ResScheduleDTO fetchScheduleById(Long id) {
        if (id < 1) {
            throw new IdInvalidException("Id không tồn tại");
        }
        Schedule scheduleDB = this.scheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("không tồn tại lịch làm việc này"));
        return ResScheduleDTO.mapEntityScheduleToDTO(scheduleDB);
    }

    @Override
    public ResPaginationDTO fetchAllSchedules(Pageable pageable, Specification<Schedule> spec) {
        Page<Schedule> pageSchedules = this.scheduleRepository.findAll(spec, pageable);
        ResPaginationDTO res = new ResPaginationDTO();
        ResPaginationDTO.Meta meta = new ResPaginationDTO.Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize((pageable.getPageSize()));
        meta.setPages(pageSchedules.getTotalPages());
        meta.setTotal((pageSchedules.getTotalElements()));
        res.setMeta(meta);
        List<ResScheduleDTO> resScheduleDTOs = pageSchedules.getContent().stream()
                .map(schedule -> ResScheduleDTO.mapEntityScheduleToDTO(schedule))
                .collect(Collectors.toList());
        res.setResult(resScheduleDTOs);
        return res;
    }

    @Override
    public ResPaginationDTO fetchSchedulesByDoctorAndDate(Long doctorId, LocalDate workDate, Pageable pageable) {
        if (doctorId < 1) {
            throw new IdInvalidException("Id bác sĩ không hợp lệ");
        }
        if (workDate == null || workDate.equals("")) {
            throw new IdInvalidException("Ngày khám không được để trống");
        }

        this.doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tồn tại bác sĩ này"));

        Page<Schedule> pageSchedules = this.scheduleRepository.findSchedulesByDoctorAndDate(doctorId, workDate,
                pageable);
        ResPaginationDTO res = new ResPaginationDTO();
        ResPaginationDTO.Meta meta = new ResPaginationDTO.Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize((pageable.getPageSize()));
        meta.setPages(pageSchedules.getTotalPages());
        meta.setTotal((pageSchedules.getTotalElements()));
        res.setMeta(meta);

        res.setResult(
                pageSchedules.getContent().stream().map(schedule -> ResScheduleDTO.mapEntityScheduleToDTO(schedule)));
        return res;

    }

    @Override
    public boolean isExistsTimeSlotBuildQuery(Schedule schedulePM) {
        int newStartTime = this.handleChangeHoursToMinutes(schedulePM.getTimeSlot().trim().split("-")[0]);
        int newEndTime = this.handleChangeHoursToMinutes(schedulePM.getTimeSlot().trim().split("-")[1]);

        List<Schedule> listDB = this.scheduleRepository.findAllSchedulesByQuery(schedulePM.getDoctor().getId(),
                schedulePM.getWorkDate());
        for (Schedule s : listDB) {
            if (schedulePM.getId() == s.getId())
                continue;
            int oldStartTime = this.handleChangeHoursToMinutes(s.getTimeSlot().trim().split("-")[0]);
            int oldEndTime = this.handleChangeHoursToMinutes(s.getTimeSlot().trim().split("-")[1]);
            if (!((newStartTime >= oldEndTime) || (newEndTime <= oldStartTime)))
                return true;
        }
        return false;
    }

    private int handleChangeHoursToMinutes(String hours) {
        String part[] = hours.split(":");
        int a = Integer.parseInt(part[0]);
        int b = Integer.parseInt(part[1]);
        return a * 60 + b;
    }
}
