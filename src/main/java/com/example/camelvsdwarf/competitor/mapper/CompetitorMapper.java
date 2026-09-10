package com.example.camelvsdwarf.competitor.mapper;

import com.example.camelvsdwarf.competitor.Competitor;
import com.example.camelvsdwarf.competitor.CompetitorRequest;
import com.example.camelvsdwarf.competitor.CompetitorResponse;

public class CompetitorMapper {

	private CompetitorMapper() {
	}

	public static CompetitorResponse toResponse(Competitor competitor) {
		return new CompetitorResponse(competitor.getId(), competitor.getName(), competitor.getNickname(),
				competitor.getType(), competitor.getDateOfBirth(), competitor.getWeight(), competitor.getHeight(),
				competitor.getOrigin(), competitor.getStatus(), competitor.getRegisteredAt(), competitor.getVictories(),
				competitor.getDefeats(), competitor.getCompletedRaces());
	}

	public static Competitor toEntity(CompetitorRequest request) {
		Competitor competitor = new Competitor();
		updateEntity(competitor, request);
		return competitor;
	}

	public static void updateEntity(Competitor competitor, CompetitorRequest request) {
		competitor.setName(request.name().trim());
		competitor.setNickname(request.nickname().trim());
		competitor.setType(request.type());
		competitor.setDateOfBirth(request.dateOfBirth());
		competitor.setWeight(request.weight());
		competitor.setHeight(request.height());
		competitor.setOrigin(request.origin() == null ? null : request.origin().trim());
	}
}
