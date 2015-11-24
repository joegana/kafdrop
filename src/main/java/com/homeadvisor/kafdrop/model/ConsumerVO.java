package com.homeadvisor.kafdrop.model;

import org.apache.commons.lang.Validate;

import java.util.*;
import java.util.stream.Collectors;

public class ConsumerVO implements Comparable<ConsumerVO>
{
   private String groupId;
   private Map<String, ConsumerTopicVO> topics = new TreeMap<>();
   private List<ConsumerRegistrationVO> activeInstances = new ArrayList<>();

   public ConsumerVO(String groupId)
   {
      Validate.notEmpty("groupId is required");
      this.groupId = groupId;
   }

   public String getGroupId()
   {
      return groupId;
   }

   public void setGroupId(String groupId)
   {
      this.groupId = groupId;
   }

   public void addActiveInstance(ConsumerRegistrationVO id)
   {
      activeInstances.add(id);
   }

   public List<ConsumerRegistrationVO> getActiveInstances()
   {
      return activeInstances;
   }

   public List<ConsumerRegistrationVO> getActiveInstancesForTopic(String topic)
   {
      return activeInstances.stream()
         .filter(reg -> reg.getSubscriptions().containsKey(topic))
         .collect(Collectors.toList());
   }

   public void addTopic(ConsumerTopicVO topic)
   {
      topics.put(topic.getTopic(), topic);
   }

   public ConsumerTopicVO getTopic(String topic)
   {
      return topics.get(topic);
   }

   public Collection<ConsumerTopicVO> getTopics()
   {
      return topics.values();
   }

   public int getActiveTopicCount()
   {
      return topics.values().stream()
         .map(t -> t.getAssignedPartitionCount() > 0 ? 1 : 0)
         .reduce(0, Integer::sum);
   }

   @Override
   public int compareTo(ConsumerVO that)
   {
      return this.groupId.compareTo(that.groupId);
   }

   @Override
   public boolean equals(Object o)
   {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      ConsumerVO vo = (ConsumerVO) o;

      if (!groupId.equals(vo.groupId)) return false;

      return true;
   }

   @Override
   public int hashCode()
   {
      return groupId.hashCode();
   }

}
