'use client';

import React from "react"

import { useState, useEffect } from 'react';
import { WorkoutPlan } from '@/lib/api';
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
import { Loader2 } from 'lucide-react';

interface WorkoutPlanModalProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  plan?: WorkoutPlan | null;
  onSubmit: (data: { name: string; description?: string }) => Promise<void>;
}

export default function WorkoutPlanModal({
  open,
  onOpenChange,
  plan,
  onSubmit,
}: WorkoutPlanModalProps) {
  const [name, setName] = useState('');
  const [description, setDescription] = useState('');
  const [isLoading, setIsLoading] = useState(false);

  useEffect(() => {
    if (plan) {
      setName(plan.name);
      setDescription(plan.description || '');
    } else {
      setName('');
      setDescription('');
    }
  }, [plan, open]);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsLoading(true);
    try {
      await onSubmit({ name, description: description || undefined });
      onOpenChange(false);
    } finally {
      setIsLoading(false);
    }
  };

  const isEdit = !!plan;

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="bg-card border-border">
        <DialogHeader>
          <DialogTitle className="text-card-foreground">
            {isEdit ? 'Edit Workout Plan' : 'Create Workout Plan'}
          </DialogTitle>
          <DialogDescription>
            {isEdit
              ? 'Update your workout plan details below.'
              : 'Create a new workout plan to organize your sessions.'}
          </DialogDescription>
        </DialogHeader>
        <form onSubmit={handleSubmit}>
          <div className="space-y-4 py-4">
            <div className="space-y-2">
              <Label htmlFor="name" className="text-foreground">Name *</Label>
              <Input
                id="name"
                placeholder="e.g., Push Pull Legs"
                value={name}
                onChange={(e) => setName(e.target.value)}
                required
                className="bg-background border-input"
              />
            </div>
            <div className="space-y-2">
              <Label htmlFor="description" className="text-foreground">Description</Label>
              <Textarea
                id="description"
                placeholder="Describe your workout plan..."
                value={description}
                onChange={(e) => setDescription(e.target.value)}
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
                'Update Plan'
              ) : (
                'Create Plan'
              )}
            </Button>
          </DialogFooter>
        </form>
      </DialogContent>
    </Dialog>
  );
}
