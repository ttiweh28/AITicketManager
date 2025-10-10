package com.aiticketmanager.service;

import com.aiticketmanager.dto.SupportAgentDTO;
import java.util.List;

public interface SupportAgentService {
    SupportAgentDTO assignTicket(Long agentId, Long ticketId);
    List<SupportAgentDTO> getAllAgents();
}
