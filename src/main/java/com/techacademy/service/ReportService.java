package com.techacademy.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.techacademy.constants.ErrorKinds;
import com.techacademy.entity.Employee;
import com.techacademy.entity.Employee.Role;
import com.techacademy.entity.Report;
import com.techacademy.repository.ReportRepository;

@Service
public class ReportService {
    private final ReportRepository reportRepository;

    @Autowired
    public ReportService(
            ReportRepository reportRepository ) {
        this.reportRepository = reportRepository;
    }

    // 日報一覧表示処理
    public List<Report> findAll() {
        return reportRepository.findAll();
    }

    //日報を取得
    public List<Report> find(UserDetail userDetail) {
        //ログインユーザーの情報を取得
        Employee employee = userDetail.getEmployee();
        //管理者権限のユーザーの場合、他従業員が登録したものを含めた全日報情報を一覧表示。
        if(employee.getRole().equals(Role.ADMIN)) {
           return reportRepository.findAll();
        //一般権限のユーザーの場合、自分が登録した日報情報のみ一覧表示。
        }else {
            return reportRepository.findByEmployee(employee);
        }

    }

    // 1件を検索
    public Report findById(Integer id) {
        // findByIdで検索
        Optional<Report> option = reportRepository.findById(id);
        // 取得できなかった場合はnullを返す
        Report report = option.orElse(null);
        return report;
    }

    // 日報保存
    @Transactional
    public ErrorKinds save(Report report, UserDetail userDetail) {

        //ログインユーザーの情報を取得
        Employee employee = userDetail.getEmployee();

        // 日付重複チェック
        ErrorKinds result = reportDateCheck(report, employee);
        if (ErrorKinds.CHECK_OK != result) {
            return result;
        }

        report.setDeleteFlg(false);

        LocalDateTime now = LocalDateTime.now();
        report.setCreatedAt(now);
        report.setUpdatedAt(now);
        report.setEmployee(employee); //ユーザー情報を登録

        reportRepository.save(report);
        return ErrorKinds.SUCCESS;
    }

    // 日報 削除
    @Transactional
    public ErrorKinds delete(Integer id) {

        Report report = findById(id);
        LocalDateTime now = LocalDateTime.now();
        report.setUpdatedAt(now);
        report.setDeleteFlg(true);

        return ErrorKinds.SUCCESS;
    }

    // 日報更新
    @Transactional
    public ErrorKinds edit(Report report) {

        //reportのユーザー情報を取得
        Employee employee = report.getEmployee();
        //今のレポートを取得
        Report oldReport = findById(report.getId());

        // 日付重複チェック
        ErrorKinds result = reportDateCheck(report, employee);
        if (ErrorKinds.CHECK_OK != result) {
            return result;
        }

        report.setDeleteFlg(false);

        LocalDateTime now = LocalDateTime.now();
        report.setCreatedAt(oldReport.getCreatedAt());
        report.setUpdatedAt(now);

        reportRepository.save(report);
        return ErrorKinds.SUCCESS;
    }

    //日付重複チェックの処理
    private ErrorKinds reportDateCheck(Report report, Employee employee) {
        //ログインユーザーの日報を検索
        List<Report> employeereport = reportRepository.findByEmployee(employee);

        //日付が同じものがあるか探す
        for(Report r : employeereport) {
            if(r.getReportDate().isEqual(report.getReportDate())) {
                // 更新時、画面で表示中の日報データを除く
                if(report.getId() != null) {
                if(report.getId().equals(r.getId())) {
                    continue;
                    }
                }

                return ErrorKinds.DATECHECK_ERROR;
            }
        }

        return ErrorKinds.CHECK_OK;
    }

}
