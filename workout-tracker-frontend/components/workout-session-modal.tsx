'use client';

import React from "react"

import { useState, useEffect } from 'react';
import { WorkoutSession, WorkoutSessionInput } from '@/lib/api';
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from '@/components/ui/dialog';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Textarea } from '@/components/ui/textarea';
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select';
import { Loader2 } from 'lucide-react';

interface WorkoutSessionModalProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  session?: WorkoutSession | null;
  planId: number;
  onSubmit: (data: Omit<WorkoutSessionInput, 'sessionExercises'>) => Promise<void>;
}

export default function WorkoutSessionModal({
  open,
  onOpenChange,
  session,
  planId,
  onSubmit,
}: WorkoutSessionModalProps) {
  const [name, setName] = useState('');
  const [notes, setNotes] = useState('');
  const [workoutDate, setWorkoutDate] = useState('');
  const [status, setStatus] = useState<'PENDING' | 'COMPLETED' | 'SKIPPED'>('PENDING');
  const [isLoading, setIsLoading] = useState(false);

  useEffect(() => {
    if (session) {
      setName(session.name);
      setNotes(session.notes || '');
      setWorkoutDate(session.workoutDate?.split('T')[0] || '');
      setStatus(session.status);
    } else {
      setName('');
      setNotes('');
      setWorkoutDate(new Date().toISOString().split('T')[0]);
      setStatus('PENDING');
    }
  }, [session, open]);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsLoading(true);
    try {
      await onSubmit({
        name,
        notes: notes || undefined,
        workoutDate,
        status,
        workoutPlanId: planId,
      });
      onOpenChange(false);
    } finally {
      setIsLoading(false);
    }
  };

  const isEdit = !!session;

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="bg-card border-border">
        <DialogHeader>
          <DialogTitle className="text-card-foreground">
            {isEdit ? 'Edit Session' : 'Create Session'}
          </DialogTitle>
          <DialogDescription>
            {isEdit
              ? 'Update your workout session details.'
              : 'Create a new workout session for this plan.'}
          </DialogDescription>
        </DialogHeader>
        <form onSubmit={handleSubmit}>
          <div className="space-y-4 py-4">
            <div className="space-y-2">
              <Label htmlFor="sessionName" className="text-foreground">Name *</Label>
              <Input
                id="sessionName"
                placeholder="e.g., Push Day A"
                value={name}
                onChange={(e) => setName(e.target.value)}
                required
                className="bg-background border-input"
              />
            </div>
            <div className="space-y-2">
              <Label htmlFor="workoutDate" className="text-foreground">Date *</Label>
              <Input
                id="workoutDate"
                type="date"
                value={workoutDate}
                onChange={(e) => setWorkoutDate(e.target.value)}
                required
                className="bg-background border-input"
              />
            </div>
            <div className="space-y-2">
              <Label htmlFor="status" className="text-foreground">Status</Label>
              <Select value={status} onValueChange={(v) => setStatus(v as typeof status)}>
                <SelectTrigger className="bg-background border-input">
                  <SelectValue placeholder="Select status" />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="PENDING">Pending</SelectItem>
                  <SelectItem value="COMPLETED">Completed</SelectItem>
                  <SelectItem value="SKIPPED">Skipped</SelectItem>
                </SelectContent>
              </Select>
            </div>
            <div className="space-y-2">
              <Label htmlFor="notes" className="text-foreground">Notes</Label>
              <Textarea
                id="notes"
                placeholder="Add any notes for this session..."
                value={notes}
                onChange={(e) => setNotes(e.target.value)}
                rows={3}
                className="bg-background border-input resize-none"
              />
            </div>
          </div>
          <DialogFooter>
            <Button
              type="button"
              variant="outline"
              onClick={() => onOpenChange(false)}
              disabled={isLoading}
            >
              Cancel
            </Button>
            <Button type="submit" disabled={isLoading}>
              {isLoading ? (
                <>
                  <Loader2 className="mr-2 h-4 w-4 animate-spin" />
                  {isEdit ? 'Updating...' : 'Creating...'}
                </>
              ) : isEdit ? (
                'Update Session'
              ) : (
                'Create Session'
              )}
            </Button>
          </DialogFooter>
        </form>
      </DialogContent>
    </Dialog>
  );
}
